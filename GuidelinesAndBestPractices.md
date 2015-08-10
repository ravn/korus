  * Processes should not be too big in execution otherwise it would end up being a sequential program.

  * Avoid too small Processes. Ideally execution time of a Process should be more than execution time taken by Korus Runtime to manage a process.

  * Run independent Processes of a use case in Parallel if feasible.

  * For Data & I/O Intensive Processes adjust Number of Executers in properties file to optimize performance.

  * Number of Schedulers can also be adjusted depending upon the amount of message or request in Korus Runtime.

  * Do not write blocking code in Processes.

  * Send small messages (parameters) in Remote Process Call.