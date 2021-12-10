# JED - Jacob's ed

Jed is a line mode text editor written in Java. It is similar to ed but not a clone, infact the commands are changed. It currently works but has much room for improvement.

## Commands

```
Jed commands:
q: quits.
w: writes the file.
w: filename: writes with inputted name.
o: filename: opens the file.
a: appends user input to the end of the file.
A: appends user input after the current line.
p: prints the file.
n: prints the file with line numbers.
c: deletes and changes the current line.
d: deletes the current line.
Any integer: changes to that line number.
g/expression/: finds and prints the expression.
%s/expression/newExpression/: replaces an expression with new user input through the whole file.
<integer, integer>: prefix works with d and s/ex/nex/ for a range of line.
s/expression/newExpression/: replaces an expression with new user input in the current line.
h: prints the commands and their description.
```

## Building Jed

```
make
sudo cp jed /usr/bin/jed && sudo cp jed.jar /usr/bin/jed.jar
```

## TODO

- Add Regex support

- check and rewrite poorly written functions