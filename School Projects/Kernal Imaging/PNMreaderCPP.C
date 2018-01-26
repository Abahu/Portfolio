#include <PNMreaderCPP.h>
#include <iostream>
#include <fstream>
#include <cstring>
#define MAGIC_NUM "P6"

PNMreaderCPP::PNMreaderCPP(char* filename)
{
	filepath = filename;
}

void PNMreaderCPP::Execute()
{
	Image* output = GetOutput();
	unsigned char magicNum[128];
	int width, height, maxval, size;

	std::ifstream ins(filepath, std::ifstream::in);

	ins >> magicNum;
	ins >> width;
	ins >> height;
	ins >> maxval;

	size = width * height * 3 * sizeof(unsigned char);
	output->SetSize(width, height);
	/*
	This ignore is required. If not used, then the next character read would be a 
	whitespace. This would throw off the pixel data. This line ignores that
	whitespace.
	*/
	ins.ignore(1); 
	//ins.seekg(1, ins.cur);
	ins.read((char*) output->getBuffer(), size);

	ins.close();
}

