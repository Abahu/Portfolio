#ifndef SOURCE_H
#define SOURCE_H
#include <image.h>
#include <iostream>
#include <logging.h>
#include <vector>
#include <sink.h>
class Source
{
protected:
	Image output;
	bool updated = false;
	std::vector<Sink*> outptrs;
public:
	virtual void Execute() = 0;
	inline virtual void callOut() { std::cerr << "I'm in a generic Source!\n"; };
	Image* GetOutput();
	virtual void Update();
	void setUpdatable();
	void addOutPointer(Sink* ptr);
	void removeOutPointer(Sink* ptr);
	Source();
};
#endif
