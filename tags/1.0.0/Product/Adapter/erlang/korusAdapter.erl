 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 % Korus - http://code.google.com/p/korus
 % Copyright (C) 2010 Impetus Technologies, Inc.(http://www.impetus.com/)
 % This file is part of Korus.
 % Korus is free software: you can redistribute it and/or modify
 % it under the terms of the GNU General Public License version 3 as published
 % by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 % 
 % Korus is distributed in the hope that it will be useful,
 % but WITHOUT ANY WARRANTY; without even the implied warranty of
 % MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 % GNU General Public License for more details.
 %    
 % You should have received a copy of the GNU General Public License
 % along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


-module(korusAdapter).
-export([connect/1,fetchData/1,listen/1,connectKorus/2,sendData/1,disconnect/0,socketManager/2,send/2,loop/4]).

-define(TCP_OPTIONS, [binary, {packet, 0}, {active, false}, {reuseaddr, true}]).
-define(CONNECT_PORT,7935).
-define(LISTEN_PORT,7936).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
send(Process, Data) ->
	korusReader! {request, Process, Data}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
socketManager(R,W)->
	receive
		{reader, Socket} ->
			socketManager(Socket,W);
		
		{writer, Socket} ->
			socketManager(R,Socket);

		{shutdown, Flag} ->
			gen_tcp:close(R),
			gen_tcp:close(W),
			exit(self(), kill)	
	end,
	socketManager(R,W).	

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
connect(Host)->
	Manager = spawn(korusAdapter,socketManager, [0,0]),	
	register(korusManager,Manager),

	connectKorus(Host,?CONNECT_PORT),
	Pid = spawn(korusAdapter,listen, [?LISTEN_PORT]),	
	register(korusWriter,Pid).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
listen(Port) ->
    {ok, LSocket} = gen_tcp:listen(Port, ?TCP_OPTIONS),
    {ok, Socket} = gen_tcp:accept(LSocket),
    korusManager ! {writer, Socket},
    fetchData(Socket).
  
fetchData(Socket)->	
	case gen_tcp:recv(Socket, 2) of
        {ok, No_of_keys} ->	
		Key_count =list_to_integer(binary_to_list(No_of_keys)),

		ParameterList = loop(Socket,0,Key_count,[]),
		Hash = dict:from_list(ParameterList),
		
		%%%%%%%%%%%%% send response data to process %%%%%%%%%%%%%
		ProcessName= dict:fetch(action,Hash),
		ProcessName! {response,Hash};

	{error, closed} ->
		io:format("error occured")
	end,
	fetchData(Socket).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

loop(Socket,N,Length,Acc) when N >= Length ->
	lists:reverse(Acc);
	

loop(Socket,N,Length,Acc) ->

	{ok,KeyLength}=gen_tcp:recv(Socket,4),
	{ok,KeyName} = gen_tcp:recv(Socket,list_to_integer(binary_to_list(KeyLength))),
	Key = list_to_atom(binary_to_list(KeyName)),

	{ok,KeyValueLength}= gen_tcp:recv(Socket,4),
	{ok,KeyValue}=gen_tcp:recv(Socket,list_to_integer(binary_to_list(KeyValueLength))),
	Value = list_to_atom(binary_to_list(KeyValue)),

	T = {Key,Value},
	
	loop(Socket,N+1,Length,[T|Acc]).



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

connectKorus(Host,Port)->
	{ok, Socket}= gen_tcp:connect(Host, Port, [binary,{packet, 0},{reuseaddr,true},{active, false}]), 
	Pid1 = spawn(korusAdapter,sendData, [Socket]),	
	register(korusReader,Pid1),
	korusManager ! {reader, Socket}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sendData(Socket)->
	receive
		{request,Action,Data} ->
		
		LengthOfAction = getFormattedLength("action"),
		LengthOfActionName = getFormattedLength(Action),				
		
		Hash=dict:to_list(Data),
		
		TotalParams = lists:flatlength(Hash)+1,
		if TotalParams<10  ->
			 FormattedTotalParams = lists:concat([0,TotalParams]);
     		true->
			FormattedTotalParams = TotalParams ,
   			false
  		end,

		
		QueryString = lists:flatmap(fun(X)->
				{Name, [Value]} = X ,
				LengthOfName = getFormattedLength(Name),				
				LengthOfValue = getFormattedLength(Value),
				lists:concat([LengthOfName,Name,LengthOfValue,Value])
   	        end, Hash),
		
			RequestObject = lists:concat([FormattedTotalParams,LengthOfAction,action,LengthOfActionName,Action,QueryString]),	
			gen_tcp:send(Socket, RequestObject)
	end,
	sendData(Socket).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
getFormattedLength(String)->
	LengthOfString = lists:flatlength(String),
	if LengthOfString<10  ->
		NewLengthOfString = lists:concat([0,0,0,LengthOfString]);
	true->
		if LengthOfString<100  ->
		  	NewLengthOfString = lists:concat([0,0,LengthOfString]);
		true->
			if LengthOfString<1000  ->
			  	NewLengthOfString = lists:concat([0,LengthOfString]);
			true->
				NewLengthOfString = LengthOfString ,
				false	
               		end											
		end
	end,
NewLengthOfString.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% Code for Clean Shutdown %%%%%%%%%%%%%%%%%%%%%%%%%

disconnect()->
	korusManager ! {shutdown, 1},
	exit(whereis(korusReader), kill),
	exit(whereis(korusWriter), kill),
	unregister(korusReader),
	unregister(korusWriter),
	unregister(korusManager).





