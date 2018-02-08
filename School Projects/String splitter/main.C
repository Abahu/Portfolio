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
bool splitIter(std::string s, std::vector<Pair>& memo);
void reconMemo(std::string s, std::vector<Pair>& memo);

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
		std::cout << "phrase number: " << i+1 << '\n' << s << "\n\n";
		

		//Create the n-memo array
		size_t size = s.size();
		// 0 = not set. 1 = false. 2 = true
		std::vector<Pair> memoRecur(size);
		std::vector<Pair> memoIter(size);

		std::cout << "iterative attempt:\n";
		bool canDo = splitIter(s, memoIter);
		if(canDo)
		{
			std::cout << "YES, can be split\n";
			reconMemo(s, memoIter);
			std::cout << '\n';
		}
		else
			std::cout << "NO, cannot be split\n\n";

		std::cout << "memoized attempt:\n";
		Pair returnValue = split(0, s, memoRecur);
		if(returnValue.first)
		{
			std::cout << "YES, can be split\n";
			reconMemo(s, memoRecur);
			std::cout << '\n';
		}
		else
			std::cout << "NO, cannot be split\n\n";
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

bool splitIter(std::string s, std::vector<Pair>& memo)
{
	int n = s.size();
	if(memo.size() == n)
		memo.resize(n + 1);

	memo[n].first = true;
	memo[n].second = n;
	//std::cerr << "Size is " << n << "and memo[" << n <<"] is " << true <<"\n";
	for(int i = n-1; i >= 0; --i)
	{
		memo[i].first = false;
		for(int j = i; j < n; ++j)
		{
			std::string sub = s.substr(i, j+1 - i);
			bool inDict = dict.isIn(sub);
			//std::cerr << "Is substring from " << i << " to " << j << ", \"" << sub <<"\" in? " << inDict << " Is the memo[" << j+1 <<"] valid? " << memo[j+1].first << "\n";
			if(inDict && memo[j+1].first)
			{
				memo[i].first = true;
				memo[i].second = j+1;
			}
		}
	}
	return memo[0].first;
}

void reconMemo(std::string s, std::vector<Pair>& memo)
{
	Pair currPair = memo[0];
	int startPos = 0;
	int len = 0;
	int size = s.size();
	do
	{
		currPair = memo[startPos];
		len = currPair.second - startPos;
		std::cerr << s.substr(startPos, len) << " ";
		startPos = currPair.second;
	}
	while(startPos != size && currPair.second != 0);
	std::cerr << '\n';
}
