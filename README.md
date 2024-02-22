
# Dongemoon Console App

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Minimum transaction algorithm

This console app calculates how much each person in a purchase should pay another person. So we need an Algorithm to minimize the amount of transactions in each period.

I started searching, and the first article I faced was [the Algorithm Behind Splitwise’s Debt Simplification Feature,](https://medium.com/@mithunmk93/algorithm-behind-splitwises-debt-simplification-feature-8ac485e97688) which seemed to be the same problem as mine. But after digging deeper into it, seeing the implementation and trying to figure it out, I found out that it might be too complicated, and I might not be able to figure it out on time before the Final project is due.

There were several Stackoverflow questions about minimizing transactions. Although none of them really helped me, but some comments pointed out good articles about this problem. The stack overflow chats really helped me understand the problems different aspects.

[stackoverflow 1](https://stackoverflow.com/questions/1163116/algorithm-to-determine-minimum-payments-amongst-a-group), [stackoverflow 2](https://stackoverflow.com/questions/974922/algorithm-to-share-settle-expenses-among-a-group), [stackoverflow 3](https://stackoverflow.com/questions/877728/what-algorithm-to-use-to-determine-minimum-number-of-actions-required-to-get-the), [stackoverflow 4](https://stackoverflow.com/questions/18771748/what-algorithms-exist-to-minimize-the-number-of-transactions-between-nodes-in-a)
Then I found a [geeks for geeks article](https://www.geeksforgeeks.org/minimize-cash-flow-among-given-set-friends-borrowed-money/) that provided an algorithm similar to what I wanted. I implemented an algorithm similar to that, but with lots of changes. Since this implementation was very simple, I needed to change the whole algorithm to make it compatible with my more complicated data types, But the idea is the same.

## Other Sources

Except for the transaction algorithm, I used other new concepts in my program. Such as:

### Exception Handling

I was familiar with the concepts, but I read about Java's exception handling implementation on  [Exception Handling in Java](https://www.baeldung.com/java-exceptions)

### Hashmap

I was familiar with the Hashmap concept as well, but I saw some examples of how Java's hashmap works on  [w3schools](https://www.w3schools.com/java/java_hashmap.asp) and [How to Iterate HashMap in Java.](https://www.geeksforgeeks.org/how-to-iterate-hashmap-in-java/)

### File IO

I used these sources to learn about writing to a file and reading data from a file in Java.

[YT Tutorial](https://www.youtube.com/watch?v=YzwiuRDgSSY)

[How to write an object to file in Java (ObjectOutputStream)](https://mkyong.com/java/how-to-write-an-object-to-file-in-java/)

[How do I save a String to a text file using Java?](https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java)
