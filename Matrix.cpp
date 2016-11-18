#include "stdafx.h"
#include "Matrix.h"
#include <iostream>


std::vector<int> Matrix::getSize()
{
	std::vector<int> size(2);
	size[0] = data.size();
	size[1] = (data.size() >= 1 ) ? data[0].size() : 0;
	return size;
}

//IO methods
void Matrix::print()
{
	std::cout << this->getSize()[0] << "x" << this->getSize()[1] << " matrix:\n";
	for each (std::vector<double> foo in data)
	{
		for each (double bar in foo)
			std::cout << bar << " ";
		std::cout << '\n';
	}
	std::cout << '\n';
}
void Matrix::output(std::ofstream& file)
{
	for each (std::vector<double> foo in data)
	{
		for each (double bar in foo)
			file << bar;
	}
	file << 1.33734;
}
std::ofstream& operator<<(std::ofstream& file, Matrix& mat)
{
	for (int i = 0; i < mat.getSize()[0]; i++)
		for (int j = 0; j < mat.getSize()[1]; j++)
			file << mat[i][j];
	//file << 1.33734;
	return file;
}
std::ifstream& operator>>(std::ifstream& file, Matrix& mat)
{
	int size[2];
	file >> size[0];
	file >> size[1];

	mat.resize(size[0], size[1]);
	for (int i = 0; i < size[0]; i++)
		for (int j = 0; j < size[1]; j++)
			file >> mat[i][j];

	return file;
}


//Vector methods
void Matrix::push_back(std::vector<double> val)
{
	data.push_back(val);
}
std::vector<double> Matrix::column(int index)
{
	std::vector<double> ret(0);
	for (int i = 0; i < this->getSize()[0]; i++)
		ret.push_back(data[i][index]);

	return ret;
}
std::vector<double> Matrix::row(int index)
{
	if (this->getSize()[0] >= 1)
		return data[index];
	else
		return std::vector<double>(1);
}
double Matrix::dot(std::vector<double> v1, std::vector<double> v2)
{
	double ret = 0.0f;
	if (v1.size() == v2.size())
	{
		for (int i = 0; i < (int)v1.size(); i++)
			ret += v1[i] * v2[i];
	}

	return ret;
}

//Standard Matrix methods
double Matrix::det()
{
	double determinant = 0;

	if (this->getSize()[0] == this->getSize()[1]) //Makes sure that this is a square matrix
	{
		if (this->getSize()[0] == 2)
		{
			determinant = (data[0][0] * data[1][1]) - (data[0][1] * data[1][0]);
		}
		else
		{
			for (int i = 0; i < this->getSize()[0]; i++)
			{
				determinant += this->cofactor(i, this->getSize()[0] - 1) * data[i][this->getSize()[0] - 1];
			}
		}
	}
	else
		determinant = -1.0f;

	return determinant;
}
Matrix Matrix::T()
{
	Matrix ret(this->getSize()[1], this->getSize()[0]);
	for (int i = 0; i < ret.getSize()[0]; i++)
	{
		ret[i] = this->column(i);
	}
	return ret;
}
Matrix Matrix::cinverse()
{
	//Work on this
	Matrix cofactors(this->getSize()[0], this->getSize()[1]);
	for (int i = 0; i < cofactors.getSize()[0]; i++)
		for (int j = 0; j < cofactors.getSize()[1]; j++)
			cofactors[i][j] = this->cofactor(i, j);
	return cofactors.T() / this->det();
}
double Matrix::minor(int index1, int index2)
{
	if (this->getSize()[0] == data[0].size())
	{
		Matrix minorMatrix = *this;
		minorMatrix.delRow(index1);
		minorMatrix.delCol(index2);
		double determinant = minorMatrix.det();
		/*std::cout << "::\n";
		minorMatrix.print();
		std::cout << "det: " << determinant << '\n';
		int code = 0;
		std::cin >> code;*/
		return determinant;
	}
	else
		return -1;
}
double Matrix::cofactor(int index1, int index2)
{
	int neg = ((index1 + index2) % 2 == 0) ? 1 : -1;
	double cof = neg*this->minor(index1, index2);
	return cof;
}

//Matrix mutator methods
void Matrix::delRow(int index)
{
	data.erase(data.begin() + index);
}
void Matrix::delCol(int index)
{
	for (int i = 0; i < this->getSize()[0]; i++)
		data[i].erase(data[i].begin() + index);
}
void Matrix::resize(int dim1, int dim2)
{
	data.resize(dim1);
	for (int i = 0; i < dim1; i++)
		data[i].resize(dim2);
}
void Matrix::set(double value, int index1, int index2)
{
	data.at(index1).at(index2) = value;
}
void Matrix::zeros()
{
	fill(0.0f);
}
void Matrix::zeros(int newBounds1, int newBounds2)
{
	fill(0.0f, newBounds1, newBounds2);
}
void Matrix::fill(double value)
{
	for (int i = 0; i < this->getSize()[0] - 1; i++)
		for (int j = 0; j < (int)data[0].size() - 1; j++)
			data[i][j] = value;
}
void Matrix::fill(double value, int newBounds1, int newBounds2)
{
	for (int i = 0; i < newBounds1 - 1; i++)
		for (int j = 0; j < newBounds2 - 1; j++)
			data[i][j] = value;
}
void Matrix::append(Matrix& toAppend, bool horizontally)
{
	data = appendRet(toAppend, horizontally).getData();
}
void Matrix::append(Matrix& toAppend)
{
	append(toAppend, true);
}
Matrix Matrix::appendRet(Matrix& toAppend, bool horizontally)
{
	int retSize[2];
	int minSize[2];
	minSize[0] = (this->getSize()[0] < toAppend.getSize()[0]) ? this->getSize()[0] : toAppend.getSize()[0];
	minSize[1] = (this->getSize()[1] < toAppend.getSize()[1]) ? this->getSize()[1] : toAppend.getSize()[1];
	retSize[0] = (this->getSize()[0] >= toAppend.getSize()[0]) ? this->getSize()[0] : toAppend.getSize()[0];
	retSize[1] = (this->getSize()[1] >= toAppend.getSize()[1]) ? this->getSize()[1] : toAppend.getSize()[1];

	if (horizontally)
		retSize[1] += minSize[1];
	else
		retSize[0] += minSize[0];

	Matrix ret(retSize[0], retSize[1]);

	if (horizontally)
	{
		std::vector<double> rowInsert(0);
		for (int i = 0; i < retSize[0]; i++)
		{
			if (i < this->getSize()[0])
				rowInsert = this->row(i);
			else
				for (int j = 0; j < this->getSize()[1]; j++)
					rowInsert.push_back(0);

			if (i < toAppend.getSize()[0])
				for each (float val in toAppend[i])
					rowInsert.push_back(val);
			else //If there is a dim mismatch, fill with zeros
				for (int i = 0; i < toAppend.getSize()[1]; i++)
					rowInsert.push_back(0);

			int si = rowInsert.size();
			ret[i] = rowInsert;
			rowInsert.clear();
		}
	}
	else
	{
		for (int i = 0; i < this->getSize()[0]; i++)
		{

			ret[i] = this->row(i);
			for (int j = 0; j < retSize[1] - this->getSize()[1]; j++)
				ret[i].push_back(0);
		}

		for (int i = this->getSize()[0]; i < ret.getSize()[0]; i++)
		{
			ret[i] = toAppend[i - this->getSize()[0]];
			for (int j = 0; j < retSize[1] - toAppend.getSize()[1]; j++)
				ret[i].push_back(0);
		}
	}

	return ret;
}
Matrix Matrix::appendRet(Matrix& toAppend)
{
	return appendRet(toAppend, true);
}
void Matrix::appendRet(Matrix& appendTo, Matrix& toAppend, bool horizontally)
{
	appendTo.append(toAppend, horizontally);
}
void Matrix::appendRet(Matrix& appendTo, Matrix& toAppend)
{
	Matrix::appendRet(appendTo, toAppend, true);
}
void Matrix::insert(std::vector<double> src, int dim, int startIndex, bool horizontally)
{
	if (src.size() < (int)(0.6 * this->getSize()[horizontally]) || (!horizontally)) 
	{
		// If the size of the source is much smaller than the target's size,
		// Then it doesn't make a lot of sense to copy the source and then append.
		// Instead, this just inserts the values of the sources
		// Yes, that is supposed to be a 0.6

		// Or, if vertically, it does this as well :)
		if (horizontally)
		{
			for (int i = 0; i < src.size(); i++)
			{
				data[dim][startIndex + i] = src[i];
			}
		}
		else
		{
			for (int i = 0; i < src.size(); i++)
			{
				data[startIndex + i][dim] = src[i];
			}
		}
	}
	else
	{
		// This appends to the source and then copies it
		for (int i = src.size(); i < this->getSize()[horizontally]; i++)
		{
			src.push_back(data[dim][i]);
		}
	}
}
void Matrix::insert(std::vector<double> src, int dim, int startIndex)
{
	insert(src, dim, startIndex, true);
}

//Matrix accessor methods
double Matrix::a(int index1, int index2)
{
	return data[index1][index2];
}
std::vector<std::vector<double>> Matrix::getData()
{
	return data;
}

//Operator Overloads
std::vector<double>& Matrix::operator[](int index)
{
	return data.at(index);
}
Matrix Matrix::operator+(Matrix& fuz)
{
	if ((fuz.getSize()[0] == this->getSize()[0]) && (fuz.getSize()[1] == data[0].size()))
	{
		Matrix ret = *this;
		for (int i = 0; i < (int) this->getSize()[0]; i++)
		{
			for (int j = 0; j < (int)data[0].size(); j++)
			{
				ret[i][j] -= fuz[i][j];
			}
		}
		return ret;
	}
	return *(new Matrix(1, 1));
}
Matrix Matrix::operator+(double num)
{
	Matrix ret = *this;
	for (int i = 0; i < this->getSize()[0]; i++)
		for (int j = 0; j < this->getSize()[1]; j++)
			ret[i][j] += num;

	return ret;
}
Matrix Matrix::operator-(Matrix& fuz)
{
	if ((fuz.getSize()[0] == this->getSize()[0]) && (fuz.getSize()[1] == data[0].size()))
	{
		Matrix ret = *this;
		for (int i = 0; i < (int) this->getSize()[0]; i++)
		{
			for (int j = 0; j < (int) data[0].size(); j++)
			{
				ret[i][j] -= fuz[i][j];
			}
		}
		return ret;
	}
	return *(new Matrix(1, 1));
}
Matrix Matrix::operator-(double num)
{
	Matrix ret = *this;
	for (int i = 0; i < this->getSize()[0]; i++)
		for (int j = 0; j < this->getSize()[1]; j++)
			ret[i][j] += num;

	return ret;
}
Matrix Matrix::operator*(Matrix& fuz)
{
	if (this->getSize()[1] == fuz.getSize()[0])
	{
		Matrix ret(this->getSize()[0], fuz.getSize()[1]);
		int s = ret.getSize()[0]; //The result is always a square matrix, so the dims are the same
		for (int i = 0; i < s; i++)
		{
			for (int j = 0; j < s; j++)
			{
				ret[i][j] = Matrix::dot(this->row(i), fuz.column(j));
			}
		}

		return ret;
	}
	else
		return Matrix(1,1);
}
Matrix Matrix::operator*(double num)
{
	Matrix ret = *this;
	for (int i = 0; i < ret.getSize()[0]; i++)
		for (int j = 0; j < ret.getSize()[1]; j++)
			ret[i][j] *= num;
	return ret;
}
Matrix Matrix::operator/(Matrix& fuz)
{
	//The dividing matrix must be invertible. Therefore, it must be square
	//The cinverse of the matrix must also follow the dimensions for matrix multiplication.
	if ((fuz.getSize()[0] == fuz.getSize()[1]) && (data[0].size() == fuz.getSize()[0]))
	{
		return *this * fuz;
	}
	else
		return *(new Matrix(1,1));
}
Matrix Matrix::operator/(double num)
{
	Matrix ret = *this;
	for (int i = 0; i < ret.getSize()[0]; i++)
		for (int j = 0; j < ret.getSize()[1]; j++)
			ret[i][j] /= num;
	return ret;
}


//Constructor
Matrix::Matrix()
{
	data.resize(1);
	data[0].resize(1);
}
Matrix::Matrix(int dim1, int dim2)
{
	data.resize(dim1);
	for (int i = 0; i < dim1; i++)
		data[i].resize(dim2);

}