#include <PNMwriter.h>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#define MAGIC_NUM "P6"

PNMwriter::PNMwriter()
{
}

void PNMwriter::Write(char* filename)
{
	Logger::LogEvent("PNMwriter: about to execute");
	if (filename == nullptr or filename == NULL)
	{
		Logger::LogEvent("Throwing exception: (PNMwriter): PNMwriter: filename wasn't specified");
		throw DataFlowException("FileIOException", "No filename was provided");
	}
	FILE* f_out = fopen(filename, "w");
	if(f_out == NULL)
	{
		Logger::LogEvent("Throwing exception: (PNMwriter): PNMwriter: file couldn't be opened for writing");
		throw DataFlowException("FileIOException", "The file couldn't be opened for writing");
	}
	int width = image1->getWidth();
	int height = image1->getHeight();
	size_t size = width * height * 3 * sizeof(unsigned char);
	fprintf(f_out, "%s\n%d %d\n%d\n", MAGIC_NUM, width, height, 255);
	if(fwrite(image1->getBuffer(), 1, size, f_out) != size)
	{
		Logger::LogEvent("Throwing exception: (PNMwriter): PNMwriter: couldn't completely write to file");
		throw DataFlowException("FileWriteException", "Couldn't write completely to the file");
	}
	fclose(f_out);
	Logger::LogEvent("PNMwriter: done executing");
}
