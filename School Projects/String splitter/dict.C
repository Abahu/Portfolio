#include "dict.h"
#include <iostream>

Dict::Dict()
{
}

void Dict::addString(std::string s)
{
	data.push_back(s);
}

bool Dict::isIn(std::string s)
{
	for(int i = 0; i < data.size(); ++i)
		if(!data[i].compare(s))
			return true;
	return false;
}
