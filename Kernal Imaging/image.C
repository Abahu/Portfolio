#include "image.h"
#include <cstring>
#include <cstdio>
#include "source.h"
Image::Image()
{
	width = 0;
	height = 0;
	buffer = nullptr;
}

void Image::operator=(Image& other)
{
	if(width != other.getWidth() || height != other.getHeight())
	{
		width = other.getWidth();
		height = other.getHeight();
		if (buffer != nullptr)
		{
			delete buffer;
		}
		buffer = new unsigned char[3 * width * height];
	}
	
	memcpy(buffer, other.getBuffer(), 3 * width * height);
}

Image::~Image()
{
	if (buffer != nullptr)
	{
		delete [] buffer;
	}
}

void Image::SetSize(int w, int h)
{
	width = w;
	height = h;
	if (buffer != nullptr)
	{
		delete [] buffer;
	}
	buffer = new unsigned char[3 * width * height];
}

void Image::setSource(void* ptr) const
{
	srcptr = ptr;
}

void* Image::getSource() const
{
	return srcptr;
}

void Image::Update() const
{
	((Source*) srcptr)->Update();
}
