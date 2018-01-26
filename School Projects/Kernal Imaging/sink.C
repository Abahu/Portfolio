#include <sink.h>
#include <source.h>
#include <iostream>
Sink::Sink()
{
	image1 = nullptr;
	image2 = nullptr;
}

void Sink::SetInput(const Image* img)
{
	if(image1 != nullptr)
		((Source*) image1->getSource())->removeOutPointer(this);
	image1 = img;
	((Source*) image1->getSource())->addOutPointer(this);
}

void Sink::SetInput2(const Image* img)
{
	if(image2 != nullptr)
		((Source*) image2->getSource())->removeOutPointer(this);
	image2 = img;
	((Source*) image2->getSource())->addOutPointer(this);
}
