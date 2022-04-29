# Java Mentoring Program: Effective Java

This maven-based project contains the exercises defined in the 'effective-java' module of Java Mentoring program.

0) To run the project one have the following options:

- mvn test. Compiles and runs the tests.
- mvn site. Compiles, runs the test and generates the following reports: jacoco coverage, mutation coverage,
  checkstyle (site)

2) LFU Cache implementation based on java code. Package: org.oka.effectivejava.cache.java.LFUCacheService.

3) LRU Cache implementation based on guava libraries. Package: org.oka.effectivejava.cache.guava.LRUCacheService

4) Binary Search

- Linear binary search implemented in: org.oka.effectivejava.search.LinearSearch (Baseline)
- Binary Iterative search implemented in: org.oka.effectivejava.search.BinaryIterativeSearch
- Binary Recursive search implemented in: org.oka.effectivejava.search.BinaryRecursiveSearch

Benchmarking comparison added as a part of the tests. The following table displays a comparison depending on the input
size (each row is an average of five times).

| Data set size | Linear search |  Binary Search (Iterative) | Binary Search (Recursive) |
|----------|:-------------:|------:|------:|
| 10000 | 40.0 ms | 2.0 ms. | 2.0 ms |
| 30000 | 194.6 ms | 4.6 ms. | 2.8 ms |
| 50000 | 621.4 ms | 7.0 ms. | 2.8 ms |
| 70000 | 1012.2 ms | 9.0 ms. | 6.6 ms |
| 90000 | 2385.2 ms | 11.6 ms. | 6.8 ms |
| 110000 | 3860.2 ms | 15.4 ms. | 8.6 ms |
| 130000 | 5130.6 ms | 18.2 ms. | 9.4 ms |
| 150000 | 6015.2 ms | 16.2 ms. | 11.0 ms |
| 170000 | 7554.8 ms | 20.2 ms. | 12.4 ms |
| 190000 | 8774.5 ms | 22.4 ms. | 13.0 ms |
| 210000 | 9397.2 ms | 27. ms. | 15.4 ms |

4) Merge Sort. Implementation org.oka.effectivejava.sort.MergeSort (Implements 'Sort' interface). Complexity (O(N log(
   N))).

5) Insertion Sort. Implementation org.oka.effectivejava.sort.InsertionSort (Implements 'Sort' interface). Complexity (O(
   N^2)).

Benchmarking comparison added as a part of the tests. The following table displays a comparison depending on the input
size.

| Data set size | Merge sort (O(N log(N))) |  Insertion sort (O(N^2)) |
|----------|:-------------:|------:|
| 10000 | 8.2 ms. | 20.0 ms.|
| 20000 | 9.0 ms. | 54.8 ms.|
| 30000 | 9.2 ms. | 120.0 ms.|
| 40000 | 16.2 ms. | 249.8 ms.|
| 50000 | 19.2 ms. | 352.8 ms.|
| 60000 | 26.0 ms. | 490.8 ms.|
| 70000 | 26.6 ms. | 818.8 ms.|
| 80000 | 30.2 ms. | 1241.0 ms.|
| 90000 | 32.6 ms. | 1472.0 ms.|
| 100000 | 32.4 ms. | 1529.4 ms.|

7) Integration of both sort mechanism in a comparison. Following a table to compare both implementation:
