# ninterpolate

A Clojure library designed to interpolate numeric tables of any dimension.

## Usage

Numeric interplolation can be done on tables of any number of dimensions.

All tables are hash-maps of independent,dependent pairs.

The simplest table is a 1-D table of independent,dependent pairs of values:

    (def table1d {1.0,2.0 3.0,4.0 7.0,6.0})

To read the table call interpolate and pass it the independent value to read the table at.

    (interpolate table1d 2.0)

The result would be 3.0

Higher dimension tables are still hashes of pairs, except the dependent values
are also hash maps of pairs.

For example, a bivariant table could look like this:

    (def table2d  
        {  
         1.0 {1,2 3,4 5,6 7,10.0}  
         2.0 {7,8 9,10.0, 11,12.0}  
        }  
    )


This table is read with two independent values:

    (interpolate table2d 1.0 1.0)

The result would be 2.0

Or,

    (interpolate table2d 1.5 7.0)

The result would be 9.0

Tables of higher dimensions are constructed the same way. A trivariant
table might look like this:

    (def table3d
      {
       0.0
        {
          1.0 {1,2 3,4 5,6 7,10.0}
          2.0 {7,8 9,10.0, 11,12.0}
        }
  
       4.0
        {
          1.5 {11,2 13,4 15,6 7,10.0}
          2.5 {17,8 19,10.0, 11,12.0}
        }
      }
    )

To read it pass 3 independent values:

    (interpolate table3d 4.0 2.0 7.0)
    
There is no limit to the number of dimensions. There is also no requirement
that each subtable use the same independent values, nor is there a requirement
that each subtable have the same number of points.

Two options can be specified on a per dimension basis. Those are the degree of
interpolation and whether or not extrapolation is allowed. Currently orders of
1 and 2 are allowed.

To specify these options change the independent values passed to the interpolate
function to be hash maps instead of values. For example, to read the 1-d table
above as a second order fit. The value itself uses a :value key.

    (interpolate table1d {:value 2.0 :order 2})
    
This time the result would be 3.083333333333333

The two options currently supported are

:order (either 1 or 2; 1 is the default)  
:extrap (true or false; false is the default)  

The options can be specified per dimension. For example

    (interpolate table2d 0.0 {:extrap true :value 8.0})
or  

    (interpolate table2d {:extrap false :value 0.0} {:extrap true :value 8.0})



This library needs more work to check for valid tables, number of independents, etc.

Stay tuned.

Eric

## License

The MIT License (MIT)

Copyright (c) 2014 Eric Meyers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
