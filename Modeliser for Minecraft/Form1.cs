using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Reflection;

namespace Modeliser_for_Minecraft
{
    public class vertex : object
    { 
        private int[] coord = new int[3];

        public void shift(int x, int y, int z)
        {
            coord[0] += x;
            coord[1] += y;
            coord[2] += z;
        }
        public void set(int x, int y, int z)
        {
            coord[0] = x;
            coord[1] = y;
            coord[2] = z;
        }
        public void set(int point, int ToB)
        {
            //0:NW, 1:NE, 2:SE, 3:SW
            switch (point)
            {
                case 0:
                    set(0, ToB, 0);
                    break;
                case 1:
                    set(0, ToB, 1);
                    break;
                case 2:
                    set(1, ToB, 1);
                    break;
                case 3:
                    set(1, ToB, 0);
                    break;
            }
        }
        public int[] ret()
        {
            return new int[] { coord[0], coord[1], coord[2] };
        }
        public vertex subtract(vertex sub)
        {
            return new vertex(coord[0] - sub.coord[0], coord[1] - sub.coord[1], coord[1] - sub.coord[1]);
        }

        public vertex()
        {
            set(0, 0, 0);
        }
        public vertex(int x, int y, int z)
        {
            set(x, y, z);
        }
    }
    public class triangle : object
    {
        private vertex[] vertices = new vertex[3];
        private int dir = 0; //0 = North, 1 = East, 2 = South, 3 = West, 4 = Bottom, 5 = Top
        private int part = 0; //0 = bottom, 1= top part

        public void shiftT(int x, int y, int z)
        { 
            foreach (vertex point in vertices)
            {
                point.shift(x, y, z);
            }
        }
        public void setT(int x, int y, int z)
        {
            setDirection(dir);
            shiftT(x, y, z);
        }
        public void setDirection(int direction)
        {
            //Super clever optimisation mode: Activate!
            for (int i = 0; i <= 2; i++)
                vertices[i] = new vertex();
            dir = direction;
            if (dir == 4 || dir == 5) //4 and 5 are the exact same thing except 5 is with the top vertices while 4 is the bottom
            {
                vertices[0].set(0, dir - 4); //The second argument is whether or not the vertex is on top or bottom, so since top is actually a value one higher
                vertices[2].set(2, dir - 4); //Than bottom, you can subtract bottom. If it is bottom, you get 0, which puts it at the bottom. If it's top, it's 1. Get it?

                vertices[1].set(1 + 2 * part, dir - 4); //Bottom part faces North, top part faces South.
            }
            else // 0-3 actually have the same exact pattern. This is useful. Since the vertices have the same directions as the  triangles up to 3, you can just do mod 4!
            {
                vertices[0].set(dir, 0);
                vertices[2].set((dir + 1) % 4, 1);
                if (part == 0)
                    vertices[1].set((dir + 1) % 4, 0);
                else
                    vertices[1].set(dir, 1);
            }
            // Look how optimised this is! It used to be like 50 lines of code. Dang. Too hot.
        }
        public void setDirection(int direction, int BoT)
        {
            part = BoT;
            setDirection(direction);
        }
        public int[] retT()
        {
            List<int> dL = new List<int>();
            foreach(vertex point in vertices)
            {
                int[] dI = point.ret();
                for (int i = 0; i < dI.Length; i++)
                {
                    dL.Add(dI[i]);
                }
            }
            return dL.ToArray();
        }
        public vertex[] getVertices()
        {
            return vertices;
        }
        public vertex getNormal()
        {
            vertex U = vertices[1].subtract(vertices[0]);
            vertex V = vertices[2].subtract(vertices[0]);

            return new vertex((U.ret()[1]*V.ret()[2])- (U.ret()[2] * V.ret()[1]),
                (U.ret()[2] * V.ret()[0]) - (U.ret()[0] * V.ret()[2]),
                (U.ret()[0] * V.ret()[1]) - (U.ret()[1] * V.ret()[0]));
        }

        public triangle()
        {
            setDirection(0);
        }
        public triangle(vertex v1, vertex v2, vertex v3)
        {
            vertices[0] = v1;
            vertices[1] = v2;
            vertices[2] = v3;
        }
        public triangle(int direction)
        {
            setDirection(direction);
        }
        public triangle(int direction, int BoT)
        {
            setDirection(direction, BoT);
        }
    }
    public class face : object
    {
        private triangle[] triangles = { new triangle(), new triangle() };
        private int dir = 0; //0 = N, 1 = W, 2 = S, 3 = W, 4 = Bottom, 5 = Top
        public void write(BinaryWriter writer)
        {
            //Currently borked! Use the ASCII version for now, please
            UInt16 bytecount = 0;

            foreach (triangle tri in triangles)
            {
                writer.Write("foreach triangle");
                for (int i = 0; i < 3; i++)
                {
                    writer.Write(Convert.ToSingle(tri.getNormal().ret()[i]));
                }
                int[] dI = tri.retT();
                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        writer.Write(Convert.ToSingle(dI[3 * i + j]));
                    }
                }
                writer.Write(bytecount);
                writer.Write("end");
            }
        }

        public void write(StringBuilder writer)
        { 
            foreach (triangle tri in triangles)
            {
                writer.AppendLine("facet normal " + Convert.ToString(tri.getNormal().ret()[0]) + " " + Convert.ToString(tri.getNormal().ret()[1]) + " " + Convert.ToString(tri.getNormal().ret()[2]));
                writer.AppendLine("outer loop");
                int[] dI = tri.retT();
                for (int i = 0; i < 3; i++)
                {
                    writer.Append("vertex ");
                    for (int j = 0; j < 3; j++)
                    {
                        writer.Append(Convert.ToString(dI[3 * i + j]));
                        writer.Append(" ");
                    }
                    writer.AppendLine("");
                }
                writer.AppendLine("endloop");
                writer.AppendLine("endfacet");
            }
        }

        public void transform(int x, int y, int z)
        {
            triangles[0].shiftT(x, y, z);
            triangles[1].shiftT(x, y, z);
        }

        public void setDirection(int direction)
        {
            dir = direction;
            triangles[0].setDirection(direction,0);
            triangles[1].setDirection(direction,1);
        }

        public face()
        {
            setDirection(0);
        }
        public face(int direction)
        {
            setDirection(direction);
        }
        public face(int x, int y, int z)
        {
            setDirection(0);
            transform(x, y, z);
        }
        public face(int direction, int x, int y, int z)
        {
            setDirection(direction);
            transform(x, y, z);
        }

    }

    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        
        private void buttonGo_Click(object sender, EventArgs e)
        {
            
            OpenFileDialog openFileDialog1 = new OpenFileDialog();
            openFileDialog1.Filter = "File|*.*";
            openFileDialog1.Title = "Open Builder Data File";

            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Assign data
                System.IO.Stream data = openFileDialog1.OpenFile();



                //Begin Data Reading
                /*
                    This is my totally convoluted and unoptimised code for seperating my data
                    The data looks like 1~1~1~1~...;
                    Each block of data has four numbers with tildes after it. The first three values are the X, Y, and Z coordinates of the block, respectively
                    The last value of the block of data is what type of block exists in this coordinate. This is only useful for the Minecraft counterpart, currently
                    The tildes are white spaces. Tilde + semicolon signifies the end of the data. Anything after the ~; is completely ignored.
                */
                debug.Text += "Processing data" + '\n';
                List<int> m = new List<int>();   
                progressBar1.Value = 0;
                int l = 0;
                while (l < data.Length)
                {
                    int pl = 1;
                    int val = 0;
                    int foo = 0;
                    do
                    {
                        foo = data.ReadByte();
                        l++;
                        progressBar1.Maximum = 3200;
                        progressBar1.Value = Convert.ToInt32(Math.Floor(progressBar1.Maximum* (Convert.ToDecimal(l) / Convert.ToDecimal(data.Length))));
                        if (foo != 126 && foo != 59)
                        {
                            val = val * pl + foo - 48;
                            pl *= 10;
                        }
                        else if (val != 00)
                                m.Add(val);
                    }
                    while (foo != 126 && foo != 59);
                }
                /*
                for (int i = 0; i < data.Length; i++)
                {
                    
                    int foo = data.ReadByte();

                        if (foo != 126)
                        {
                            nibble[l] = foo - 48;
                            l++;
                        }
                        else
                        {
                            int p;
                            if (nibble[1] != 0)
                            {
                                p = 10 * nibble[0] + nibble[1];
                            }
                            else
                            {
                                p = nibble[0];
                            }

                            //debug.Text += p.ToString() + " ";
                            nibble[0] = 0; nibble[1] = 0;
                            m.Add(p);
                            l = 0;
                        }
                    
                    progressBar1.Value = Convert.ToInt32(Math.Floor(100 * (Convert.ToDecimal(i + 1) / Convert.ToDecimal(data.Length))));
                } */
                int[] dataNew = m.ToArray();
                m.Clear();

                //Find the max and min dimensions
                //Exactly what it says on the tin. This figures out how big I need to make my array. If the maximum dimension in the X direction is 64, but the max in the Y is 2,
                //I don't want to have a 64x64xZ array and waste tons of data. This is just optimisation
                int[] maxDim = new int[3];
                int[] minDim = { 500, 500, 500 };
                for (int i = 0; i < dataNew.Length - 1; i++)
                {
                    int r = i % 4;
                    if (r != 3)
                    {
                        if (dataNew[i] > maxDim[r])
                            maxDim[r] = dataNew[i];

                        if (dataNew[i] < minDim[r])
                            minDim[r] = dataNew[i];
                    }
                }

                //Initialise main data array
                //This makes the data managable for me
                int[, ,] dataArray = new int[maxDim[0]+1, maxDim[1]+1, maxDim[2]+1];

                //Transpose data to main array
                for (int i = 0; i < dataNew.Length/4; i++)
                {
                    dataArray[dataNew[i * 4], dataNew[i * 4 + 1], dataNew[i * 4 + 2]] = dataNew[i * 4 + 3];
                }
                dataNew = new int[0];

                //End Data Reading

                //Begin Face Creation
                List<face> faces = new List<face>();
                //faces.Add(new face());

                //for (int i = 0; i <= 5; i++)
                //    faces.Add(new face(i));


                //This Face Creation protocol doesn't check for adjacency. Instead, it just makes faces for each block.

                /*
                for (int i = minDim[0]; i <= maxDim[0]; i++)
                {
                    for (int j = minDim[1]; j <= maxDim[1]; j++)
                    {
                        for (int k = minDim[2]; k <= maxDim[2]; k++)
                        {
                            for (int n = 0; n <= 5; n++)
                                faces.Add(new face(n, i, j, k));
                        }
                    }
                }
                //*/

                //This Face Creation protocol checks if there are adjacent blocks. If there is an adjacent block,
                //A face isn't made there. This prevents file bloating.
                
                debug.Text += "Creating faces" + '\n';
                for (int i = minDim[0]; i <= maxDim[0]; i++)
                {
                    for (int j = minDim[1]; j <= maxDim[1]; j++)
                    {
                        for (int k = minDim[2]; k <= maxDim[2]; k++)
                        {
                            if (dataArray[i, j, k] != 0)
                            {
                                // North
                                if (i - 1 >= 0)
                                {
                                    if (dataArray[i - 1, j, k] == 0)
                                        faces.Add(new face(0, i, j, k));
                                }
                                else
                                    faces.Add(new face(0, i, j, k));

                                // South
                                if (i + 1 <= maxDim[0])
                                {
                                    if (dataArray[i + 1, j, k] == 0)
                                        faces.Add(new face(2, i, j, k));
                                }
                                else
                                    faces.Add(new face(2, i, j, k));

                                // Bottom
                                if (j - 1 >= 0)
                                {
                                    if (dataArray[i, j - 1, k] == 0)
                                        faces.Add(new face(4, i, j, k));
                                }
                                else
                                    faces.Add(new face(4, i, j, k));

                                // Top
                                if (j + 1 <= maxDim[1])
                                {
                                    if (dataArray[i, j + 1, k] == 0)
                                        faces.Add(new face(5, i, j, k));
                                }
                                else
                                    faces.Add(new face(5, i, j, k));

                                //West
                                if (k - 1 >= 0)
                                {
                                    if (dataArray[i, j, k - 1] == 0)
                                        faces.Add(new face(3, i, j, k));
                                }
                                else
                                    faces.Add(new face(3, i, j, k));

                                //East
                                if (k + 1 <= maxDim[2])
                                {
                                    if (dataArray[i, j, k + 1] == 0)
                                        faces.Add(new face(1, i, j, k));
                                }
                                else
                                    faces.Add(new face(1, i, j, k));
                            }
                        }
                    }
                }
                //*/
                //End Face Creation
                dataArray = new int[0,0,0];

                //Begin File Writing

                //String Version

                //Basic ASCII file saving. Heavier on memory than binary, but easier to manage/read
                StringBuilder sb = new StringBuilder();
                string name = saveFileDialog1.FileName;
                var outPutDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().CodeBase);
                var iconPath = Path.Combine(outPutDirectory, "output.stl");
                string icon_path = new Uri(iconPath).LocalPath;
                sb.AppendLine("solid output");
                foreach (face f in faces)
                {
                    f.write(sb);
                }
                sb.AppendLine("endsolid output");

                using (StreamWriter outfile = new StreamWriter(icon_path))
                {
                    outfile.Write(sb.ToString());
                }

                debug.Text = "Done";
                //Binary Format

                
                /*
                Doesn't work ATM. Use String Version please.
                using (FileStream outfile = new FileStream(icon_path, FileMode.Create))
                {
                    //outfile.Write(sb.ToString());
                    BinaryWriter bw = new BinaryWriter(outfile);
                    uint[] header = new uint[80];
                    
                    UInt32 num = Convert.ToUInt32(faces.Count*2);
                    foreach (uint doof in header)
                    {
                        bw.Write(doof);
                    }
                    bw.Write(num);
                    //bw.WriteByte(num);
                    foreach (face Face in faces)
                    {
                        Face.write(bw);
                    }
                }*/


                //End File Writing    
            }

        }

    }
}
