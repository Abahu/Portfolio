#ifndef SINK_H
#define SINK_H
#include <image.h>
#include <stdio.h>
#include <logging.h>
class Sink
{
protected:
	const Image* image1;
	const Image* image2;
public:
	virtual void SetInput(const Image* img);
	virtual void SetInput2(const Image* img);
	inline const Image* GetInput() { return image1; }
	inline const Image* GetInput2() { return image2; }
	inline virtual bool isFilter() { return false; } //Used in updates
	Sink();
};

#endif
