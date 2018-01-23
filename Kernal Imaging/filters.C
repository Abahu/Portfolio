#include <filters.h>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <fstream>
#define MAGIC_NUM "P6"

void Filter::Update()
{
	if(updated) return; // Checks if we've already ran. If so, don't do the work again
	if(image1 != nullptr)
	{
		char l[256];
		char l2[256];
		strcpy(l, name());
		strcpy(l2, name());
		strcat(l, ": about to update input1");
		Logger::LogEvent(l);

		((Source*) image1->getSource())->Update();

		strcat(l2, ": done updating input1");
		Logger::LogEvent(l2);
	}
	else
	{
		char l[256];
		strcpy(l, name());
		strcat(l, ": input1 does not seem to exist. This might be an error");
		Logger::LogEvent(l);
	}
	if(image2 != nullptr)
	{
		char l[256];
		char l2[256];
		strcpy(l, name());
		strcpy(l2, name());
		strcat(l, ": about to update input2");
		Logger::LogEvent(l);

		((Source*) image2->getSource())->Update();

		strcat(l2, ": done updating input2");
		Logger::LogEvent(l2);
	}
	char l[256];
	strcpy(l, name());
	strcat(l, ": about to execute");
	Logger::LogEvent(l);

	Execute();

	char l2[256];
	strcpy(l2, name());
	strcat(l2, ": done executing");
	Logger::LogEvent(l2);
	updated = true;
}

void Filter::SetInput(const Image* img)
{
	Sink::SetInput(img);
	setUpdatable();
}

void Filter::SetInput2(const Image* img)
{
	Sink::SetInput2(img);
	setUpdatable();
}

void Shrinker::Execute()
{
	//const Image* input = image1;
	if(image1 == nullptr || image1 == NULL)
	{
		Logger::LogEvent("Throwing exception: (Shrinker): no input!");
		throw DataFlowException("DataFlowException", "Input image not set");
	}
	output.SetSize(image1->getWidth() / 2, image1->getHeight() / 2);
	int w = output.getWidth();
	int h = output.getHeight();
	unsigned char* outbuff = output.getBuffer();
	unsigned char* inbuff = image1->getBuffer();
	for(int i = 0; i < h; ++i)
		for(int j = 0; j < w; ++j)
		{
			int index = 3*(i*w + j);
			int index2 = 3*(i*4*w + j*2);
			outbuff[index] = inbuff[index2];
			outbuff[index + 1] = inbuff[index2 + 1];
			outbuff[index + 2] = inbuff[index2 + 2];
		}
}

void LRConcat::Execute()
{
	//const Image* li = image1;
	//const Image* ri = image2;
	char buff[256];
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (LRConcat): LRConcat: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	if(image2 == nullptr || image2 == NULL)
	{
		Logger::LogEvent("Throwing exception: (LRConcat): LRConcat: no input2!");
		throw DataFlowException("NullImageException", "Input image 2 not set");
	}
	//Image* out = GetOutput();
	if(image1->getHeight() != image2->getHeight())
	{
		snprintf(buff, 256, "Throwing exception: (LRConcat): LRConcat: heights must match: %d, %d", image1->getHeight(), image2->getHeight());
		Logger::LogEvent(buff);
		throw DataFlowException("DimensionMismatchException", "Height mismatch between images");
	}
	output.SetSize(image1->getWidth() + image2->getWidth(), image1->getHeight());

	//Left image first
	int oh = output.getHeight();
	int lw = image1->getWidth() * 3;
	int rw = image2->getWidth() * 3;
	unsigned char* buffptr = output.getBuffer();
	unsigned char* libuff = image1->getBuffer();
	unsigned char* ribuff = image2->getBuffer();
	for(int i = 0; i < oh; ++i)
	{
		memcpy(buffptr, libuff, lw);
		buffptr += lw;
		libuff += lw;
		memcpy(buffptr, ribuff, rw);
		buffptr += rw;
		ribuff += rw;
	}
}


void TBConcat::Execute()
{
	//const Image* ti = image1;
	//const Image* bi = image2;
	//Image* out = GetOutput();
	char buff[256];
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (TBConcat): TBConcat: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	if(image2 == nullptr || image2 == NULL)
	{
		Logger::LogEvent("Throwing exception: (TBConcat): TCConcat: no input2!");
		throw DataFlowException("NullImageException", "Input image 2 not set");
	}
	if(image1->getWidth() != image2->getWidth())
	{
		snprintf(buff, 256, "Throwing exception: (TBConcat): TBConcat: widths must match: %d, %d", image1->getWidth(), image2->getWidth());
		Logger::LogEvent(buff);
		throw DataFlowException("DimensionMismatchException", "Width mismatch between images");
	}
	int bottomIndex = image1->getWidth() * image1->getHeight() * 3;
	output.SetSize(image1->getWidth(), image1->getHeight() + image2->getHeight());
	memcpy(output.getBuffer(), image1->getBuffer(), bottomIndex);
	memcpy(output.getBuffer() + bottomIndex, image2->getBuffer(), image2->getWidth() * image2->getHeight() * 3);
}


void Blender::Execute()
{
	//const Image* in1 = image1;
	//const Image* in2 = image2;
	//Image* out = GetOutput();
	char buff[256];
	if(image1 == nullptr || image1== NULL )
	{
		Logger::LogEvent("Throwing exception: (Blender): Blender: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	if(image2 == nullptr || image2 == NULL)
	{
		Logger::LogEvent("Throwing exception: (Blender): Blender: no input2!");
		throw DataFlowException("NullImageException", "Input image 2 not set");
	}
	if(image1->getWidth() != image2->getWidth() || image1->getHeight() != image2->getHeight())
	{
		snprintf(buff, 256, "Throwing exception: (Blender): Blender: widths and heights must match: %d, %d    %d, %d", image1->getWidth(), image1->getHeight(), image2->getWidth(), image2->getHeight());
		Logger::LogEvent(buff);
		throw DataFlowException("DimensionMismatchException", "Dimension mismatch between images");
	}
	if(factor < 0.0 || factor > 1.0)
	{
		snprintf(buff, 256, "Throwing exception: (Blender): Blender: Invalid factor for Blender: %f", factor);
		Logger::LogEvent(buff);
		throw DataFlowException("InvalidParameterException", "Factor for Blender is out of range [0.0, 1.0]");
	}
	output.SetSize(image1->getWidth(), image1->getHeight());
	int size = output.getWidth() * output.getHeight() * 3;
	unsigned char* outbuff = output.getBuffer();
	unsigned char* in1buff = image1->getBuffer();
	unsigned char* in2buff = image2->getBuffer();
	float nfactor = 1.0 - factor;
	for(int i = 0; i < size; ++i)
	{
		float image1Pix = (float) (*in1buff);
		float image2Pix = (float) (*in2buff);
		image1Pix *= factor;
		image2Pix *= nfactor;
		*outbuff = int(image1Pix + image2Pix);
		in1buff++;
		in2buff++;
		outbuff++;
	}
}

Colour::Colour(int nWidth, int nHeight, unsigned char r, unsigned char g, unsigned char b)
{
	width = nWidth;
	height = nHeight;
	colour[0] = r;
	colour[1] = g;
	colour[2] = b;
}

void Colour::Execute()
{
	output.SetSize(width, height);
	int size = width * height;
	unsigned char* buff = output.getBuffer();
	for(int i = 0; i < size; ++i)
	{
		*(buff++) = colour[0];
		*(buff++) = colour[1];
		*(buff++) = colour[2];
	}
}

void Colour::Update()
{
	Logger::LogEvent("Colour: about to execute");
	Execute();
	Logger::LogEvent("Colour: done executing");
}

void Mirror::Execute()
{
	if(image1 == nullptr || image1 == NULL)
	{
		Logger::LogEvent("Throwing exception: (Mirror): no input!");
		throw DataFlowException("Mirror", "no input!");
	}
	int width = image1->getWidth();
	int width2 = (width+1)/2;
	int height = image1->getHeight();
	output.SetSize(width, height);
	
	unsigned char* ilbuff = image1->getBuffer(); // Buffer set to the top-leftmost pixel in the image
	unsigned char* irbuff = image1->getBuffer() + (width * 3) - 3; // Buffer set to the top-rightmost pixel in the image
	unsigned char* olbuff = output.getBuffer(); // Buffer set to the top-leftmost pixel in the image
	unsigned char* orbuff = output.getBuffer() + (width * 3) - 3; // Buffer set to the top-rightmost pixel in the image

	//TODO: Switch the width with the height for better caching performance
	for(int i = 0; i < width2; ++i)
	{
		unsigned char* ilvbuff = ilbuff; // The buffer for the left column
		unsigned char* irvbuff = irbuff; // The buffer for the right column
		unsigned char* olvbuff = olbuff; // The buffer for the left column
		unsigned char* orvbuff = orbuff; // The buffer for the right column
		for(int j = 0; j < height; ++j)
		{
			// Swap red channel
			olvbuff[0] = irvbuff[0];
			orvbuff[0] = ilvbuff[0];
			
			// Swap green channel
			olvbuff[1] = irvbuff[1];
			orvbuff[1] = ilvbuff[1];

			// Swap blue channel
			olvbuff[2] = irvbuff[2];
			orvbuff[2] = ilvbuff[2];
			
			// Move the pixel pointer down the column
			ilvbuff += 3*width;
			irvbuff += 3*width;
			olvbuff += 3*width;
			orvbuff += 3*width;
		}
		ilbuff += 3;
		irbuff -= 3;
		olbuff += 3;
		orbuff -= 3;
	}
}

void Rotate::Execute()
{
	if(image1 == nullptr || image1 == NULL)
	{
		Logger::LogEvent("Throwing exception: (Rotate): no input!");
		throw DataFlowException("Rotate", "no input!");
	}
	int width = image1->getWidth();
	int height = image1->getHeight();
	output.SetSize(height, width); // Switch because of the transpose
	unsigned char* ibuff = image1->getBuffer();
	unsigned char* obuff = output.getBuffer() + (3 * height) - 3;

	char msg[256];
	for(int i = 0; i < width; ++i)
	{
		unsigned char* ivbuff = ibuff;
		unsigned char* ovbuff = obuff;
		for(int j = 0; j < height; ++j)
		{
			ovbuff[0] = ivbuff[0];
			ovbuff[1] = ivbuff[1];
			ovbuff[2] = ivbuff[2];
			ovbuff -= 3;
			ivbuff += 3*width;
		}
		ibuff += 3;
		obuff += 3*height;
	}
}

void Subtract::Execute()
{
	char buff[256];
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (Subtract): Subtract: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	if(image2 == nullptr || image2 == NULL)
	{
		Logger::LogEvent("Throwing exception: (Subtract): Subtract: no input2!");
		throw DataFlowException("NullImageException", "Input image 2 not set");
	}
	if(image1->getWidth() != image2->getWidth() || image1->getHeight() != image2->getHeight())
	{
		snprintf(buff, 256, "Throwing exception: (Subtract): Subtract: widths and heights must match: %d, %d    %d, %d", image1->getWidth(), image1->getHeight(), image2->getWidth(), image2->getHeight());
		Logger::LogEvent(buff);
		throw DataFlowException("DimensionMismatchException", "Dimension mismatch between images");
	}
	output.SetSize(image1->getWidth(), image1->getHeight());
	unsigned char* i1buff = image1->getBuffer();
	unsigned char* i2buff = image2->getBuffer();
	unsigned char* obuff = output.getBuffer();
	int acc;
	int size = 3 * image1->getWidth() * image2->getHeight();
	for(int i = 0; i < size; ++i)
	{
		acc = *(i1buff++);
		acc -= *(i2buff++);
		if(acc < 0) acc = 0;
		*(obuff++) = acc;
	}
}

void Greyscale::Execute()
{
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (Grayscale): Grayscale: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	output.SetSize(image1->getWidth(), image1->getHeight());
	unsigned char* ibuff = image1->getBuffer();
	unsigned char* obuff = output.getBuffer();
	int size = image1->getWidth() * image1->getHeight();
	int grey;
	char buff[256];
	for(int i = 0; i < size; ++i)
	{
		grey = ibuff[0]/5 + ibuff[1]/2 + ibuff[2]/4;
		*(obuff++) = grey;
		*(obuff++) = grey;
		*(obuff++) = grey;
		ibuff += 3;
	}
}

void Blur::Execute()
{
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (Blur): Blur: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	output.SetSize(image1->getWidth(), image1->getHeight());
	int width = image1->getWidth();
	int width3 = width*3;
	int height = image1->getHeight();
	unsigned char* obuff = output.getBuffer() + 3*(width+1);
	unsigned char* itbuff = image1->getBuffer();
	unsigned char* imbuff = itbuff + 3*width;
	unsigned char* ibbuff = imbuff + 3*width;
	for(int j = 1; j < height-1; ++j)
	{
		for(int i = 3; i < width3-3; ++i)
		{
			*(obuff++) = 	itbuff[0]/8 + itbuff[3]/8 + itbuff[6]/8 + 
					imbuff[0]/8 + 		    imbuff[6]/8 + 
					ibbuff[0]/8 + ibbuff[3]/8 + ibbuff[6]/8;
			++itbuff;
			++imbuff;
			++ibbuff;
		}
		//Skipping two pixels. We're currently 1 away from the end of the row and we need to be 1 away from the beginning of the next row
		obuff += 6;
		itbuff += 6;
		imbuff += 6;
		ibbuff += 6;
	}
	// Now copy the edges
	unsigned char* olbuff = output.getBuffer();
	unsigned char* orbuff = output.getBuffer() + 3*(width - 1);
	unsigned char* ilbuff = image1->getBuffer();
	unsigned char* irbuff = image1->getBuffer() + 3*(width - 1);
	for(int j = 0; j < height; ++j)
	{
		olbuff[0] = ilbuff[0];
		olbuff[1] = ilbuff[1];
		olbuff[2] = ilbuff[2];
		orbuff[0] = irbuff[0];
		orbuff[1] = irbuff[1];
		orbuff[2] = irbuff[2];
		olbuff += 3*width;
		orbuff += 3*width;
		ilbuff += 3*width;
		irbuff += 3*width;
	}
	unsigned char* otbuff = output.getBuffer();
	unsigned char* obbuff = output.getBuffer() + (height - 1) * (width * 3);
	itbuff = image1->getBuffer();
	ibbuff = image1->getBuffer() + (height - 1) * (width * 3);;
	for(int j = 0; j < width; ++j)
	{
		otbuff[0] = itbuff[0];
		otbuff[1] = itbuff[1];
		otbuff[2] = itbuff[2];
		obbuff[0] = ibbuff[0];
		obbuff[1] = ibbuff[1];
		obbuff[2] = ibbuff[2];
		otbuff += 3;
		obbuff += 3;
		itbuff += 3;
		ibbuff += 3;
	}
}

void CheckSum::OutputCheckSum(const char* path)
{
	if(image1 == nullptr || image1 == NULL )
	{
		Logger::LogEvent("Throwing exception: (Checksum): Checksum: no input1!");
		throw DataFlowException("NullImageException", "Input image 1 not set");
	}
	unsigned char* buff = image1->getBuffer();
	unsigned char r = 0;
	unsigned char g = 0;
	unsigned char b = 0;
	long size = image1->getWidth() * image1->getHeight();
	for(int i = 0; i < size; ++i)
	{

		r += *(buff++);
		g += *(buff++);
		b += *(buff++);
	}
	char msg[256];
	sprintf(msg, "CHECKSUM: %d, %d, %d \n", (unsigned int) r, (unsigned int) g, (unsigned int) b);
	
	std::ofstream out(path, std::ofstream::out);
	out << msg;
	out.close();

}
