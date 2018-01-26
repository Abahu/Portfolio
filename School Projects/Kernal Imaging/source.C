#include <source.h>
#include <iostream>
#include <filters.h>
Image* Source::GetOutput()
{
	return &output;
}

Source::Source()
{
	output.setSource(this);
}

void Source::Update()
{
	if(updated) return;
	Execute();
	updated = true;
}

void Source::addOutPointer(Sink* ptr)
{
	outptrs.push_back(ptr);
}

void Source::removeOutPointer(Sink* ptr)
{
	size_t outsize = outptrs.size();
	for(int i = 0; i < outsize; ++i)
	{
		if(outptrs[i] == ptr)
		{
			outptrs.erase(outptrs.begin() + i);
			return;
		}
	}
}

void Source::setUpdatable()
{
	// Signal that we need to be rerun
	updated = false;
	size_t size = outptrs.size();
	// Go up the dependencies chain and recursively set them to be updated if
	// they are filters or a source.
	for(int i = 0; i < size; ++i)
		if(outptrs[i]->isFilter())
			((Filter*) outptrs[i])->setUpdatable();
}
