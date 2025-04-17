/* By Fraser Raney and Shaili Trivedi
 * Purpose
 * The purpose of this class is to represent particular CPU processes in the simulation which are either regular or 
 * interrupt processes. The probability of a regular process is 0.9 and 0.1 for interrupts.
 * Fields
 * jobs have a number:
 * regular jobs are numbered sequentially starting at one, interrupt jobs are always 0
 * jobs have a number of processors:
 * from 1 to parameter P
 * jobs have a time required for processing:
 * the time has an average of parameter T, the job's time is found by Tln(u) u is an uniform random number b/n 0 and 1 
 */
using System;
namespace assign2
{
    class Job
    {
        // Fields
        public int Number { get; set; }
        public int Processors { get; set; }
        public double InterTime { get; }
        public double ProcessTime { get; set; }

        // Time complexity of all methods: O(1)

        // Constructor (3-args): used to reset the processing time of a pre-empted job
        public Job(int num, int pro, double pt)
        {
            Number = num;
            Processors = pro;
            ProcessTime = pt;
        }
        // Constructor (5-args): used to create new jobs)
        public Job(int num, double u, double m, double t, int p)
        {
            if (Convert.ToInt16(Math.Floor(u * 10)) < 9)
            {
                Number = num;
            }
            else
            {
                Number = 0;
            }
            InterTime = (-1 * m * Math.Log(u));
            ProcessTime = (-1 * t * Math.Log(u));
            Processors = Convert.ToInt16(Math.Floor(u * (p + 1 - 1) + 1));
        }

        // ToString override: used in debugging
        public override string ToString()
        {
            return String.Format("Job Instance: Number {0}, Processors: {1}, Inter-Arrival Time: {2}, Process Time: {3}",
                Number, Processors, InterTime, ProcessTime);
        }
    }
}