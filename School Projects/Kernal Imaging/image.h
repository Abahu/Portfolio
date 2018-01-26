#ifndef IMAGE_HEADER
#define IMAGE_HEADER
class Image
{
private:
	unsigned char* buffer;
	int width;
	int height;
	mutable void* srcptr;
public:	
	Image();
	void operator=(Image& other);
	~Image();
	
	void SetSize(int x, int y);
	inline int getWidth() const { return width; }
	inline int getHeight() const { return height; }
	inline unsigned char* getBuffer() const { return buffer; }
	void setSource(void* ptr) const;
	void* getSource() const;
	void Update() const;
};
#endif
