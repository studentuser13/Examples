/* Provided by COIS 2020H course notes
 * Purpose
 * This class creates a linked queue of generic type items.
 * Data Dictionary
 * Fields
 * Head stores a Node object used to keep track of the first item in the the queue
 * Tail stores a Node object used to keep track of the last item in the queue
 * Count stores a int representing the length of the queue
 */
using System;
namespace assign2
{

    public interface IContainer<T>
    {
        void MakeEmpty();  // Reset an instance to empty
        bool Empty();      // Test if an instance is empty 
        int Size();        // Return the number of items in an instance
    }
    public interface IQueue<T> : IContainer<T>
    {
        void Add(T item);  // Add an item to the back of a queue
        void Remove();     // Remove an item from the front of a queue
        T Front();         // Return the front item of a queue
    }

    // Queue
    // Behavior:  First-In, First-Out (FIFO)
    // Implementation:  Singly-linked list

    public class LinkedQueue<T> : IQueue<T>
    {
        private Node<T> head;   // Reference to the head (front) item
        private Node<T> tail;   // Reference to the tail (back) item
        private int count;      // Number of items in a queue

        // Time complexity of all methods: O(1)

        public LinkedQueue()
        {
            MakeEmpty();
        }

        public void Add(T item)
        {
            Node<T> newNode = new Node<T>(item, null);
            if (count == 0)
            {
                head = tail = newNode;
            }
            else
            {
                tail = tail.Next = newNode;
            }
            count++;
        }

        public void Remove()
        {
            if (!Empty())
            {
                head = head.Next;
                count--;
            }
        }

        public T Front()
        {
            if (!Empty())
                return head.Item;
            else
                return default(T);
        }

        public void MakeEmpty()
        {
            head = tail = null;
            count = 0;
        }

        public bool Empty()
        {
            return count == 0;
        }

        public int Size()
        {
            return count;
        }
    }
}