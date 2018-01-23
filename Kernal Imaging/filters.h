#include <image.h>
#include <sink.h>
#include <source.h>

class Filter : public Sink, public Source 
{
private:
public:
	virtual void Update();
	virtual const char* name() { return "Filter"; }
	inline virtual bool isFilter() { return true; }
	virtual void SetInput(const Image* img);
	virtual void SetInput2(const Image* img);
};

class Shrinker : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Shrinker"; }
};

class LRConcat : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "LRConcat"; }
};

class TBConcat : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "TBConcat"; }
};

class Blender : public Filter
{
private:
	float factor;
public:
	virtual void Execute();
	inline void SetFactor(float f) { factor = f; }
	inline virtual const char* name() { return "Blender"; }
};

class Colour : public Source
{
private:
	unsigned char colour[3];
	int width;
	int height;
public:
	Colour(int nWidth, int nHeight, unsigned char r, unsigned char g, unsigned char b);
	virtual void Execute();
	virtual void Update();
};
typedef Colour Color; // I can't bring myself to type "Color" each time. It physically feels weird.

class Mirror : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Mirror"; }
};

class Rotate : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Rotate"; }
};

class Subtract : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Subtract"; }
};

class Greyscale : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Grayscale"; } // Conforming to make your lives easier
};
typedef Greyscale Grayscale; // It's Grey not Gray, darn it!

class Blur : public Filter
{
public:
	virtual void Execute();
	inline virtual const char* name() { return "Blur"; }
};

class CheckSum : public Sink
{
public:
	void OutputCheckSum(const char* path);
};
