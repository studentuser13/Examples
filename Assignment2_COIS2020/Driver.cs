/* By Fraser Raney and Shaili Trivedi
 * Purpose
 * This program implements a discrete simulation of processes in a CPU over a two hour period. The 
 * number of processors, the mean inter-arrival time between jobs and the mean processing time for a 
 * particular job are given by the user. It also calculates and display the average wait-times.
 * 
 * Data dictionary
 * Constants
 * END_TIME stores an end-time for the simulation which is 2 hours or 7200 seconds
 * 
 * Variables
 * dateTime stores a DateTime object used to format the time in output messages, initialized to 9:00 am
 * time stores a double representing the current time determined by the simulation, initialized to 0
 * random stores a Random object used to calculate the properties of a particular Job
 * jobNum stores an int represting the number of a particular job, initialized to 1
 * numArrival stores an int which is used to distinguish multiple processor jobs, initialized to 0
 * numJob stores an int which is used to distinguish multiple processor jobs, initialized to 0
 * numDeparture stores an int which is used to distinguish multiple processor jobs, initialized to 0
 * inWait stores an int of the sum of the time that an Event object is placed in the waiting queue, initialized to 0
 * outWait stores an int of the sum of the time that an Event object is removed from the waiting queue, initialized to 0
 * numWait stores an int of the total number of Events that were put in the waiting queue, initialized to 0
 * numEvents stores an int of the total number of Events that were created in the simulation, initialized to 0
 * found stores a boolean used to determine if a particular processor has been found for an arrival event, initialized to false
 * m stores a double mean inter-arrival time inputed by the user, initialized to -1
 * t stores a double mean processing time for jobs inputed by the user, initialized to -1
 * p stores an int number of processors used in the simulation and is inputed by the user, initialized to -1
 * waitingQueue store a LinkedQueue of Events used to store arrivals and departure to be processed
 * u stores a double used to calulate the inter-arrival, processing, type and number of processors for a particular job
 * 
 * Variables determined by user input
 * processors stores an array of ints to represent the job that is being processed in a particular processor, intitialized to size p
 * priority stores a PriorityQueue of Events used to determine the next future event, intialized to size p + p + p
 * j stores a Job object represting a particular process in the CPU in the simulation
 * e stores an Event object used to represent the state of a particular Job object in the simulation
 */
using System;
namespace assign2
{
    class Driver
    {
        static void Main()
        {
            // Constants
            const double END_TIME = 7200;
            // Variables
            DateTime dateTime = new DateTime(1, 1, 1, 9, 0, 0);
            double time = 0;
            Random random = new Random();
            int jobNum = 1;
            // Distinguishes multiple processor jobs
            int numArrival = 0;
            int numJob = 0;
            int numDeparture = 0;
            // Used to calculate the mean waiting time
            double inWait = 0;
            double outWait = 0;
            int numWaitEvents = 0;
            int numEvents = 0;
            bool found = false;
            // Input variables
            double m = -1;
            double t = -1;
            int p = -1;

            // Display purpose
            Console.WriteLine("PURPOSE:\nThis program implements a discrete simulation of processes in a CPU over \na " +
                "two hour period and returns the average wait-time for jobs that were in \nqueue to be processed and the " +
                "average wait-time for all jobs.\n");

            // Get input from the user for m p and t with input validation
            do
            {
                Console.Write("Number of processors for the simulation? ");
                try
                {
                    p = Convert.ToInt32(Console.ReadLine());
                    if (p <= 0)
                        throw new ApplicationException("The number of processors must be a positive integer.");
                }
                catch (FormatException fe)
                {
                    Console.WriteLine("ERROR: {0}\nEnter a integer.\n", fe.Message);
                }
                catch (ApplicationException ae)
                {
                    Console.WriteLine("Error: {0}\nEnter a positive integer.\n", ae.Message);
                }

            } while (p <= 0);
            do
            {
                Console.Write("Mean inter-arrival time (in seconds)? ");
                try
                {
                    m = Convert.ToDouble(Console.ReadLine());
                    if (m <= 0)
                        throw new ApplicationException("The inter-arrival time must be a positive number.");
                }
                catch (FormatException fe)
                {
                    Console.WriteLine("ERROR: {0}\nEnter a number.\n", fe.Message);
                }
                catch (ApplicationException ae)
                {
                    Console.WriteLine("Error: {0}\n", ae.Message);
                }

            } while (m <= 0);
            do
            {
                Console.Write("Mean processing time for jobs (in seconds)? ");
                try
                {
                    t = Convert.ToDouble(Console.ReadLine());
                    if (t <= 0)
                        throw new ApplicationException("The processing time must be a positive number.");
                }
                catch (FormatException fe)
                {
                    Console.WriteLine("ERROR: {0}\nEnter a number.\n", fe.Message);
                }
                catch (ApplicationException ae)
                {
                    Console.WriteLine("Error: {0}\nEnter a positive number.\n", ae.Message);
                }

            } while (t <= 0);

            // Set-up the simulation according to the user's input
            // Create p number of processors
            int[] processors = new int[p];
            // Create the waiting queue
            LinkedQueue<Event> waitingQueue = new LinkedQueue<Event>();
            // Create the priority queue
            // The size of the the priority queue is accomodates upto p departures per processor, up to 
            // p arrival events per job and up to p arrival events each time the arrival events for a job are processed
            PriorityQueue<Event> priority = new PriorityQueue<Event>(p + p + p);

            // Initial arrival job event arrives at 9:00 am
            double u = random.NextDouble();
            Job j = new Job(jobNum, u, m, t, p);
            // Regular event
            if (j.Number > 0)
            {
                for (int i = 0; i < j.Processors; i += 1)
                {
                    Event e = new Event(j);
                    e.Time = j.InterTime + time;
                    priority.Add(e);
                    numEvents++;
                }
            }
            else // Interrupt event
            {
                jobNum--;
                Event e = new Event(j);
                e.Type = -1;
                e.Processor = j.Processors - 1;
                // Set the interrupt arrival event's time
                e.Time = j.InterTime + time;
                priority.Add(e);
                numEvents++;
            }

            // Main loop: while the priority queue isn't empty
            while (priority.Front() != null)
            {
                //Process next arrival event
                while (priority.Front() != null && priority.Front().Type == 1)
                {
                    Event arrival = priority.Front();
                    priority.Remove();
                    // Process arrival events while their time is less than the end time of the simulation 
                    if (arrival.Time < END_TIME)
                    {
                        numArrival = arrival.Job.Number;
                        // Reset the time and diaplay in the output
                        dateTime = new DateTime(1, 1, 1, 9, 0, 0);
                        dateTime = dateTime.AddSeconds(arrival.Time);
                        time = arrival.Time;

                        found = false;
                        // Search for an available processor
                        for (int k = 0; k < processors.Length && !found; k++)
                        {
                            if (processors[k] == 0)
                            {
                                arrival.Type = 0;
                                // Set the departure event's time
                                arrival.Time = arrival.Job.ProcessTime / arrival.Job.Processors + time;
                                arrival.Processor = k;
                                // Keep track of it in the processor array
                                processors[k] = arrival.Job.Number;
                                priority.Add(arrival);
                                // Display output
                                Console.WriteLine("Job {0} arrives and begins execution on processor {1} at {2}",
                                    arrival.Job.Number, arrival.Processor + 1, dateTime.ToShortTimeString());
                                found = true;
                            }
                        }
                        // Send it to the waiting cue if a processor was not assigned to it
                        if (arrival.Processor == -1)
                        {
                            inWait += time;
                            arrival.Time = 0;
                            numWaitEvents += 1;
                            waitingQueue.Add(arrival);
                            Console.WriteLine("Job {0} arrives and is placed in the waiting queue", arrival.Job.Number);
                        }

                        // Create a new job if all the parts of the current arrival event for a particular job have become departures
                        if (arrival.Job.Number != numJob)
                        {
                            numJob = arrival.Job.Number;
                            u = random.NextDouble();
                            j = new Job(++jobNum, u, m, t, p);
                            if (j.Number > 0) // Regular event
                            {
                                for (int i = 0; i < j.Processors; i += 1)
                                {
                                    Event e = new Event(j);
                                    // Set the arrival event's time
                                    e.Time = j.InterTime + time;
                                    priority.Add(e);
                                    numEvents++;
                                }
                            }
                            else // Interrupt event
                            {
                                jobNum--; // Avoids skipping jobs
                                Event e = new Event(j);
                                e.Type = -1;
                                e.Processor = j.Processors - 1;
                                // Set the interrupt arrival event's time
                                e.Time = j.InterTime + time;
                                priority.Add(e);
                                numEvents++;
                            }
                        }
                    }
                }

                // Process the next interrupt arrival event
                if (priority.Front() != null && priority.Front().Type == -1)
                {
                    Event interrupt = priority.Front();
                    priority.Remove();

                    // Process interrupt arrival events while their time is less than the end time of the simulation 
                    if (time < END_TIME)
                    {
                        //Reset the time and display in the output
                        dateTime = new DateTime(1, 1, 1, 9, 0, 0);
                        dateTime = dateTime.AddSeconds(interrupt.Time);
                        time = interrupt.Time;
                        // Process the interrupt
                        switch (processors[interrupt.Processor])
                        {
                            case 0: // The processor is available, so send the interrupt departure event to the priority queue
                                processors[interrupt.Processor] = -1;
                                // Set the departure event's time
                                interrupt.Time = interrupt.Job.ProcessTime + time;
                                interrupt.Type = 0;
                                priority.Add(interrupt);
                                // Display output
                                Console.WriteLine("Job {0} arrives and begins execution on processor {1} at {2}",
                                             interrupt.Job.Number, interrupt.Processor + 1, dateTime.ToShortTimeString());
                                break;
                            case -1: // Another interrupt is being processed
                                Console.WriteLine("Interrupt for processor {0} is ignored at {1}", interrupt.Processor + 1,
                                    dateTime.ToShortTimeString());
                                break;
                            default: // Pre-empt the job currently processing
                                Event bump = priority.GetAndRemove(interrupt.Processor);
                                // Create a new job with the same job number and a reduced processing time and its departure event
                                Job bumpJob = new Job(bump.Job.Number, bump.Job.Processors, (bump.Time - time) * bump.Job.Processors);
                                Event pending = new Event(bumpJob);
                                // Send the interrupt departure event to the priotity queue and keep track of it in the processor array
                                processors[interrupt.Processor] = -1;
                                interrupt.Time = interrupt.Job.ProcessTime + time;
                                interrupt.Type = 0;
                                priority.Add(interrupt);
                                // Send the pre-empted event to the waiting queue and keep track of the waiting time
                                waitingQueue.Add(pending);
                                inWait += time;
                                numWaitEvents += 1;
                                // Display output
                                Console.WriteLine("Job {0} arrives and begins execution on processor {1} at {2}, " +
                                    "pre-empting Job {3}", interrupt.Job.Number, interrupt.Processor + 1,
                                    dateTime.ToShortTimeString(), bump.Job.Number);
                                break;
                        }

                        //Create the next job
                        u = random.NextDouble();
                        j = new Job(++jobNum, u, m, t, p);
                        if (j.Number > 0) // Regular event
                        {
                            for (int i = 0; i < j.Processors; i += 1)
                            {
                                Event e = new Event(j);
                                // Set the arrival event's time
                                e.Time = j.InterTime + time;
                                priority.Add(e);
                                numEvents++;
                            }
                        }
                        else // Interrupt event
                        {
                            jobNum--; // Avoids skipping jobs
                            Event e = new Event(j);
                            e.Type = -1;
                            e.Processor = j.Processors - 1;
                            // Set the interrupt arrival event's time
                            e.Time = j.InterTime + time;
                            priority.Add(e);
                            numEvents++;
                        }
                    }
                }

                // Process the next departure event
                while (priority.Front() != null && priority.Front().Type == 0)
                {
                    Event departure = priority.Front();
                    priority.Remove();
                    numDeparture = departure.Job.Number;
                    // Reset the time and display in the output
                    dateTime = new DateTime(1, 1, 1, 9, 0, 0);
                    dateTime = dateTime.AddSeconds(departure.Time);
                    time = departure.Time;
                    // Clear the processor that finished executing
                    processors[departure.Processor] = 0;

                    // Check if there are pending events in the waiting queue, if there are, processes the oldest 
                    if (!waitingQueue.Empty())
                    {
                        Event pending = waitingQueue.Front();
                        outWait += time;
                        // Set the departure event's time
                        pending.Time = pending.Job.ProcessTime / pending.Job.Processors + time;
                        waitingQueue.Remove();
                        pending.Processor = departure.Processor;
                        processors[departure.Processor] = pending.Job.Number;
                        pending.Type = 0;
                        priority.Add(pending);
                        // Display output
                        Console.WriteLine("Job {0} completes execution and Job {1} begins execution on processor {2} at {3}",
                            departure.Job.Number, pending.Job.Number, departure.Processor + 1, dateTime.ToShortTimeString());
                    }
                    else Console.WriteLine("Job {0} completes execution on processor {1} at {2}",
                        departure.Job.Number, departure.Processor + 1, dateTime.ToShortTimeString());
                }
            }
            // Calculate average wait-time
            // Do not divide by 0
            if (numWaitEvents == 0)
                numWaitEvents = 1;
            if (numEvents == 0)
                numEvents = 1;

            // Display average wait-time for event in the waiting queue
            dateTime = new DateTime(1, 1, 1, 0, 0, 0, 0);
            dateTime = dateTime.AddSeconds((outWait - inWait) / numWaitEvents);
            Console.WriteLine("\nAverage wait-time in the waiting queue: {0}", dateTime.ToString("HH:mm:ss.fff"));
            // Display the average wait-time for all events
            dateTime = new DateTime(1, 1, 1, 0, 0, 0, 0);
            dateTime = dateTime.AddSeconds((outWait - inWait) / numEvents);
            Console.WriteLine("\nAverage wait-time for all jobs: {0} \n", dateTime.ToString("HH:mm:ss.fff"));
            Console.WriteLine("\nPress any key ...");
            Console.ReadKey();
        }

    }

}