#!/usr/bin/env python

import sys

matrix = []
rows = 0

# Prepare triangle from stdin
lines = sys.stdin.read()
for line in lines.splitlines():
    elems = line.split(" ")
    matrix.append(list(map(int, elems)))
    rows += 1

# go up to find a path O((width + height) / 2)
for row in reversed(range(rows)):
    row_len = row + 1
    for cell in range(row_len - 1):
        first = matrix[row][cell]
        second = matrix[row][cell + 1]
        if first < second:
            matrix[row-1][cell] += first
        else:
            matrix[row-1][cell] += second

# go down to recreate trace O(height)
result = []
cell = 0
for row in range(rows - 1):
    current = matrix[row][cell]
    first = matrix[row+1][cell]
    second = matrix[row+1][cell+1]
    if first < second:
        # move left in the tree
        result.append(current - first)
    else:
        result.append(current - second)
        # move right in the tree
        cell += 1

# Append leaf to the results
result.append(matrix[rows - 1][cell])
max = matrix[0][0]

# Handle output format
result_str = map(str, result)
result_str = " + ".join(result_str)
print("Minimal path is: {} = {}".format(result_str, max))
