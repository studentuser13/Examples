// By Fraser Raney and Shaili Trivedi
/* Purpose
 * The purpose of this class is to keep track of paricular processes in the simulation, ordered by their future time, 
 * that is that at which they will occur. In order to do so, this class implements ICompareable.
 * Fields
 * Job stores a Job object representing a particular CPU process
 * Type stores an int that is 1 for arrival events, 0 for departure events and -1 for interrupt arrival events
 * Time stores a double representing the future time in seconds the event will happen
 * Processor stores an intre
 * the time has an average of parameter T, the job's time is found by Tln(u) u is an uniform random number b/n 0 and 1 
 */
using System;
namespace assign2
{
    class Event : IComparable
    {
        // Fields
        public Job Job { get; }
        public int Type { get; set; }
        public double Time { get; set; }
        public int Processor { get; set; }

        // Time complexity of all methods: O(1)

        // Constructor
        public Event(Job j)
        {
            Job = j;
            Type = 1; // -1 for interrupts, 0 for departures, 1 for arrivals: 1 is default
            Processor = -1; // index of the processor of the departure event, -1 indicates that a processor has not been assigned: -1 is default
        }
        // COmpareTo method
        public int CompareTo(Object obj)
        {
            //Ordered by Time
            if (obj == null)
            {
                return 1;
            }
            Event other = obj as Event;
            if (other == null)
            {
                return 1;
            }
            else
                return other.Time.CompareTo(Time);

        }

        // ToString override: used in debugging
        public override string ToString()
        {
            return String.Format("Event Instance: Job {0}, Type: {1}, Time: {2} Processor: {3}", Job.Number, Type, Time, Processor);
        }
    }
}