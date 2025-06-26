/* Provided by COIS 2020H course notes, and modified with a method for the CPU simulation
 * Purpose
 * This class creates a priority queue of generic type items which implement IComparable. The 
 * comments descibing the modified method can be found at the method GetAndRemove at the end of this class
 * Data Dictionary
 * Fields
 * Capacity stores the length of the prioity queue's array
 * A stores an array of generic type items
 * Count stores a int representing the number of item in the priotity queue
 */
using System;
namespace assign2
{
    public interface IPriorityQueue<T> : IContainer<T> where T : IComparable
    {
        void Add(T item);  // Add an item to a priority queue
        void Remove();     // Remove the item with the highest priority
        T Front();         // Return the item with the highest priority
    }

    // Priority Queue
    // Implementation:  Binary heap

    public class PriorityQueue<T> : IPriorityQueue<T> where T : IComparable
    {
        private int capacity;  // Maximum number of items in a priority queue
        private T[] A;         // Array of items
        private int count;     // Number of items in a priority queue

        public PriorityQueue(int size)
        {
            capacity = size;
            A = new T[size + 1];  // Indexing begins at 1
            count = 0;
        }

        // Percolate up from position i in a priority queue

        private void PercolateUp(int i)
        // (Worst case) time complexity: O(log n)
        {
            int child = i, parent;

            while (child > 1)
            {
                parent = child / 2;
                if (A[child].CompareTo(A[parent]) > 0)
                // If child has a higher priority than parent
                {
                    // Swap parent and child
                    T item = A[child];
                    A[child] = A[parent];
                    A[parent] = item;
                    child = parent;  // Move up child index to parent index
                }
                else
                    // Item is in its proper position
                    return;
            }
        }

        public void Add(T item)
        // Time complexity: O(log n)
        {
            if (count < capacity)
            {
                A[++count] = item;  // Place item at the next available position
                PercolateUp(count);
            }
        }

        // Percolate down from position i in a priority queue

        private void PercolateDown(int i)
        // Time complexity: O(log n)
        {
            int parent = i, child;

            while (2 * parent <= count)
            // while parent has at least one child
            {
                // Select the child with the highest priority
                child = 2 * parent;    // Left child index
                if (child < count)  // Right child also exists
                    if (A[child + 1].CompareTo(A[child]) > 0)
                        // Right child has a higher priority than left child
                        child++;

                if (A[child].CompareTo(A[parent]) > 0)
                // If child has a higher priority than parent
                {
                    // Swap parent and child
                    T item = A[child];
                    A[child] = A[parent];
                    A[parent] = item;
                    parent = child;  // Move down parent index to child index
                }
                else
                    // Item is in its proper place
                    return;
            }
        }

        public void Remove()
        // Time complexity: O(log n)
        {
            if (!Empty())
            {
                // Remove item with highest priority (root) and
                // replace it with the last item
                A[1] = A[count--];

                // Percolate down the new root item
                PercolateDown(1);
            }
        }

        public T Front()
        // Time complexity: O(1)
        {
            if (!Empty())
            {
                return A[1];  // Return the root item (highest priority)
            }
            else
                return default(T);
        }

        // Create a binary heap
        // Percolate down from the last parent to the root (first parent)

        private void BuildHeap()
        // Time complexity: O(n)
        {
            int i;
            for (i = count / 2; i >= 1; i--)
            {
                PercolateDown(i);
            }
        }

        // Sorts and returns the InputArray

        public void HeapSort(T[] inputArray)
        // Time complexity: O(n log n)
        {
            int i;

            capacity = count = inputArray.Length;

            // Copy input array to A (indexed from 1)
            for (i = capacity - 1; i >= 0; i--)
            {
                A[i + 1] = inputArray[i];
            }

            // Create a binary heap
            BuildHeap();

            // Remove the next item and place it into the input (output) array
            for (i = 0; i < capacity; i++)
            {
                inputArray[i] = Front();
                Remove();
            }
        }

        public void MakeEmpty()
        // Time complexity: O(1)
        {
            count = 0;
        }

        public bool Empty()
        // Time complexity: O(1)
        {
            return count == 0;
        }

        public int Size()
        // Time complexity: O(1)
        {
            return count;
        }

        // By Fraser Raney and Shaili Trivedi
        // GetAndRemove method: modified for the CPU simulation
        // This method finds an item in the priority queue array, removes the item and re-orders the 
        // heap, and return the removed item
        public T GetAndRemove(int processor)
        //Time complexity: O(n)
        {
            /* Local Variables
             * item stores an generic type object of the priority queue array
             * index stores an int representing the index of a paricular item in the priority array
             * found stores a boolean used to determine if the query has been found at an array index
             */
            T item = default(T);
            int index = 1;
            bool found = false;

            for (index = 1; index < count + 1 && !found; ++index) //O(n)
            {
                Event x = A[index] as Event;
                if (processor.CompareTo(x.Processor) == 0)
                {
                    // Get query
                    item = A[index];
                    found = true;
                }
            }
            index--;
            // Relace with last item
            A[index] = A[count--];
            // Percolate down from the new item at the index
            PercolateDown(index); //O(log n)
            return item;
        }
    }

    //-------------------------------------------------------------------------
}