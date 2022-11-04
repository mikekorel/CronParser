# Cron Parser

## Brief Description
This application takes a list of tasks, each running at least daily, which are scheduled with a simplified
cron. It then finds when each of them will next run.

An example scheduler config looks like this:
```
30 1 /bin/run_me_daily
45 * /bin/run_me_hourly
* * /bin/run_me_every_minute
* 19 /bin/run_me_sixty_times
```

The first field is the minutes past the hour, the second field is the hour of the day and the
third is the command to run. For both cases * means that it should run for all values of that
field. In the above example run_me_daily has been set to run at 1:30am every day and
run_me_hourly at 45 minutes past the hour every hour. The fields are whitespace separated
and each entry is on a separate line.

When fed this config to stdin and the simulated 'current time' in the format
HH:MM as command line argument, the application outputs the soonest time at which each of the commands will fire and whether it is today or
tomorrow.
When the task should fire at the simulated 'current time' then that is the time outputted,
not the next one.


For example, given the above examples as input and the simulated 'current time'
command-line argument 16:10, the output should be:
```
1:30 tomorrow - /bin/run_me_daily
16:45 today - /bin/run_me_hourly
16:10 today - /bin/run_me_every_minute
19:00 today - /bin/run_me_sixty_times
```


## How to Run
The executable .jar file is located in the executable directory in the root of the project.
This directory also contains an example config file. You need to have Java installed on your
machine and the environment variables set up.

Apart from the config file, the application is expecting one argument representing the current
time in the HH:MM format. To run the application with the default config and for the current time
being 16:10, run this command:
`java -jar CronParser.jar 16:10 < config.txt`


## Testing
The application has some tests that check the functionality of the parser. You can find and run
them from the MainKtTest.kt file.
