#include <PNMreader.h>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#define MAGIC_NUM "P6"

PNMreader::PNMreader(char* filename)
{
	filepath = filename;
	//updated = 0;
}

void PNMreader::Execute()
{
	Logger::LogEvent("PNMreader: about to execute");
	Image* output = GetOutput();
	char magicNum[128];
	int width, height, maxval, size;
	FILE* f_in = fopen(filepath, "r");
	if(f_in == NULL)
	{
		Logger::LogEvent("Throwing Exception: (PNMreader): PNMreader: file couldn't be opened");
		throw DataFlowException("FileIOException", "The specified file could not be opened");
	}
	size_t stop_yelling;
	stop_yelling = fscanf(f_in, "%s\n%d %d\n%d\n", magicNum, &width, &height, &maxval);
	if(strcmp(MAGIC_NUM, magicNum))
	{
		Logger::LogEvent("Throwing Exception: (PNMreader): PNMreader: Magic number isn't P6");
		throw DataFlowException("FileFormatError", "The file is not of format P6");
	}
	if(maxval != 255)
	{
		Logger::LogEvent("Throwing Exception: (PNMreader): PNMreader: Maxval isn't 255");
		throw DataFlowException("FileFormatError", "The file doesn't have a maxval of 255");
	}
	size = width * height * 3 * sizeof(unsigned char);
	output->SetSize(width, height);
	if(fread((void*) output->getBuffer(), 1, size, f_in) != size)
	{
		Logger::LogEvent("Throwing exception: (PNMreader): PNMreader: file didn't contain the expected number of bytes!");
		throw DataFlowException("FileReadException", "File read did not return the expected number of bytes");
	}
	fclose(f_in);
	Logger::LogEvent("PNMreader: done executing");
}

