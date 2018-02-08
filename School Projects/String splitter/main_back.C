#include "dict.h"
#include <iostream>
#include <fstream>
#include <utility>

static Dict dict;

struct Pair
{
	bool visited = false;
	bool first;
	int second; // The next index in memo for this list
};

Pair split(int i, std::string s, std::vector<Pair>& memo);
int main()
{
	
	// Load up our dictionary
	std::string s;

	std::ifstream inf("diction10k.txt", std::ifstream::in);
	while(inf.peek() != EOF)
	{
		while(!isalpha(inf.peek()) && inf.peek() != EOF)
		{
			//std::cerr << "i ";
			inf.ignore(1);
		}
		inf >> s;
		//std::cerr << s << '\n';
		dict.addString(s);
	}

	int lines;
	std::cin >> lines;
	for(int i = 0; i < lines; ++i)
	{
		std::cin >> s;
		std::cerr << s << '\n';;

		//Create the n-memo array
		size_t size = s.size();
		// 0 = not set. 1 = false. 2 = true
		std::vector<Pair> memo(size);

		Pair returnValue = split(0, s, memo);

		std::cerr << "Can this be split? " << returnValue.first << "\n";
		if(returnValue.first)
		{
			Pair currPair;
			int startPos = 0;
			int len = 0;
			do
			{
				currPair = memo[startPos];
				len = currPair.second - startPos;
				//std::cerr << "start: " << startPos << " second: " << currPair.second << " is: " << s.substr(startPos, len) << " ";
				std::cerr << s.substr(startPos, len) << " ";
				startPos = currPair.second;
			}
			while(startPos != size && currPair.second != 0);
			std::cerr << '\n';
		}
	}
}


Pair split(int i, std::string s, std::vector<Pair>& memo)
{
	//std::cerr << "Starting recursive call at " << i << "\n";
	int n = s.size();
	if(i >= n)
	{
		Pair ret;
		ret.visited = true;
		ret.first = true;
		ret.second = n;
		return ret;
		//std::cerr << "At " << i << " we reached the end of the string\n";
	}
	
	if(memo[i].visited)
	{
		//std::cerr << "Index " << i << " was memoised: " << memo[i].first << " " << memo[i].second << "\n";
		return memo[i];
	}
	for(int j = i; j < n; ++j)
	{
		bool boolRet = dict.isIn(s.substr(i, j+1 - i));
		//std::cerr << "Is "<< s.substr(i, j+1 - i) << " in the dictionary? " << boolRet << "\n";
		if(!boolRet)
			continue;

		Pair splitRet = split(j+1, s, memo);
		boolRet &= splitRet.first;
		//std::cerr << "Is this substring valid? " << boolRet << '\n';
		if(!boolRet)
			continue;

		memo[i].visited = true;
		memo[i].first = boolRet;
		memo[i].second = j+1;
		//std::cerr << "Index " << i << " was visited: " << memo[i].first << " " << memo[i].second << "\n";
		return memo[i];
	}
	memo[i].visited = true;
	memo[i].first = false;
	memo[i].second = 0;
	return memo[i];
}
