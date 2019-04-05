Minimum tree path
=====

This simple script implements algorithm for finding path giving minimum sum
in a "triangle-like" structure. E.g.:
```
  1
 2 3
4 5 6
```

should give 1 + 2 + 4 = 7

Requirements
------------

In order to run this project Python has to be installed in the system

Run
---

    $ cat << EOF | ./mtp.py
    > 1
    > 2 3
    > 99 99 1
    > EOF
    Minimal path is: 1 + 3 + 1 = 5
