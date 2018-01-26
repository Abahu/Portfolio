#include <PNMwriterCPP.h>
#include <fstream>
#include <iostream>
#include <cstdio>
#include <cstring>
#define MAGIC_NUM "P6"

PNMwriterCPP::PNMwriterCPP()
{
}

void PNMwriterCPP::Write(char* filename)
{
	if (filename == nullptr)
	{
		fprintf(stderr, "No filename submitted. Aborting\n");
		abort();
	}
	std::ofstream outs(filename, std::ios::out);
	//FILE* f_out = fopen(filename, "w");
	if(!outs)
	{
		fprintf(stderr, "File returned NULL. Returning\n");
	}
	Image* image1 = GetInput();
	int width = image1->getWidth();
	int height = image1->getHeight();
	size_t size = width * height * 3 * sizeof(unsigned char);
	outs << MAGIC_NUM << std::endl << width << ' ' << height << std::endl << "255" << std::endl;
	//fprintf(f_out, "%s\n%d %d\n%d\n", MAGIC_NUM, width, height, 255);
	outs.write((char*) image1->getBuffer(), size);
	outs.close();
}
