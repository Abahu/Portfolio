#pragma once
#include "stdafx.h"
#include <vector>
#include <fstream>

//IO stream
//std::ofstream& operator<<(std::ofstream& file, Matrix& mat);

class Matrix
{
private:
	std::vector<std::vector<double>> data;

public:
	std::vector<int> getSize();
	

	//IO methods
	void print();
	void output(std::ofstream& file);

	//Vector methods
	void push_back(std::vector<double> val);
	std::vector<double> column(int index);
	std::vector<double> row(int index);

	//Dot and cross product
	static double dot(std::vector<double> v1, std::vector<double> v2);

	//Standard Matrix methods
	double det();			//Determinant
	Matrix T();				//Transformation
	Matrix cinverse();		//Cofactor inverse
	double minor(int index1, int index2);
	double cofactor(int index1, int index2);

	//Matrix mutator methods
	void delRow(int index);
	void delCol(int index);
	void resize(int dim1, int dim2);
	void set(double value, int index1, int index2);
	void zeros();
	void zeros(int newBounds1, int newBounds2);
	void fill(double value);
	void fill(double value, int newBounds1, int newBounds2);
	void append(Matrix& toAppend, bool horizontally);
	void append(Matrix& toAppend);
	Matrix appendRet(Matrix& toAppend, bool horizontally);
	Matrix appendRet(Matrix& toAppend);
	void static appendRet(Matrix& appendTo, Matrix& toAppend, bool horizontally);
	void static appendRet(Matrix& appendTo, Matrix& toAppend);
	void insert(std::vector<double> src, int dim, int startIndex, bool horizontally);
	void insert(std::vector<double> src, int dim, int startIndex);

	//Matrix accessor methods
	double a(int index1, int index2);
	std::vector<std::vector<double>> getData();

	//Operator overloads
	std::vector<double>& operator[](int index);
	Matrix operator+(Matrix& fuz);
	Matrix operator+(double num);
	Matrix operator-(Matrix& fuz);
	Matrix operator-(double num);
	Matrix operator*(Matrix& fuz);
	Matrix operator*(double num);
	Matrix operator/(Matrix& fuz);
	Matrix operator/(double num);
	friend std::ofstream& operator<<(std::ofstream& file, Matrix& mat);
	friend std::ifstream& operator>>(std::ifstream& file, Matrix& mat);
	

	
	//Constructor and destructor
	Matrix();
	Matrix(int dim1, int dim2);
	//~Matrix();
};