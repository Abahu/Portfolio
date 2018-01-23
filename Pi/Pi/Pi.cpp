// Pi.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <iomanip>
#include <math.h>
#include <time.h>

using namespace std;


long double getPi(long int terms);
int getTermValue(long int prec);
long int charArrayToNumber(char oldArr[]);
//bool checkType(void* val);

int main()
{
	long int precision = 0;
	cout << std::fixed;
	cout << setprecision(20);
	bool running = true;
	char input[80];
	int test;
	//cin >> test;
	//cout << test << '\n';
	//cin >> test;
	while (running)
	{
		cout << "Enter how many terms (iterations) you want.\nType 'e' without quotes to exit.\n";
		cin >> input;
		
		if (input[0] == 'e')
		{
			break;
		}
		precision = charArrayToNumber(input);
		if (input[0] == '0' || (input[0] == '-' && input[1] == '0'))
			cout << "Cannot have '0' iterations.\n";
		else if (precision == 0)
			cout << "Number is not valid.\n";
		else if (precision < 0)
			cout << "Number must be greater than 0.\n";
		else
		{
			long double piFourth = getPi(precision);
			//cout << "pi/4 = " << piFourth << '\n';
			cout << "pi = " << piFourth * 4 << "\n\n";
		}
	}
	
    return 0;
}

struct RESULT
{
	bool isValid;
	int value;
};

int getTermValue(long int prec) //Get how many terms it takes to be precise to the indicated digit
{
	long double pi = acosl(-1.0L);
	// ^ a thousand digits of pi umad?
	long double valPi = 0L;
	long double valPiNex = 1L;
	int iter = 0;
	while (floor((valPiNex - valPi)*pow(10,(prec - 1))) != 0)
	{
		iter++;
		valPi = 4 * getPi(iter);
		valPiNex = 4 * getPi(iter + 1);
	}

	return iter;
}

long int charArrayToNumber(char oldArr[])
{
	long int ret = 0;
	std::string trunc(oldArr);
	trunc.shrink_to_fit();
	char* arr = (char*)trunc.c_str();
	int retMod = 1;
	for (unsigned int i = 0; i < trunc.length(); i++)
	{
		if (arr[i] == '-' && i == 0)
			retMod = -1;
		
		if ((int)arr[i] >= 0x30 && (int)arr[i] <= 0x39)
			ret += ((int)arr[i] - 0x30) * (trunc.length() - i);
		else
			return 0;
	}

	return ret * retMod;
}

long double getPi(long int terms)
{
	long double pi = 0;
	long double flipper = -1;
	long double denominator = 1;
	for (long int i = 0; i < terms; i++)
	{
		flipper *= -1;
		denominator = 1 + 2*i;
		pi += flipper / denominator;
	}

	return pi;
}