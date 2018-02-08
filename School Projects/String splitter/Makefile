SRC=main.C dict.C
OBJ=$(SRC:.C=.o)

prog: $(OBJ)
	g++ $(OBJ) -std=c++11 -o a6

.C.o: $<
	g++ -O2 -std=c++11 -I. -c $<

clean:
	rm *.o proj3G
