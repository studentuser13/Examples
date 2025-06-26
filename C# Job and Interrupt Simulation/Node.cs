/* Provided by COIS 2020H course notes
 * Purpose
 * This class makes generic Nodes that are used to make singly-linked lists.
 * Data Dictionary
 * Fields
 * Item stores a generic item of type T
 * Next stores a Node that is the next part of a singly-linked list
 */
using System;
namespace assign2
{
    class Node<T>
    {
        //Properties
        public T Item { get; set; }
        public Node<T> Next { get; set; }

        // Time complexity of all methods: O(1)

        //Constuctor (0 args)
        public Node()
        {
            Next = null;

        }

        //Constructor (2 args)
        public Node( T item, Node<T> next)
        {
            Item = item;
            Next = next;
        }
    }
}
