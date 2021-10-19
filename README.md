# KE.Net PPG

KE.Net PPG is an Android application that implements getting the PPG signal from a Polar OH1 or Verity Sense. Currently this is working, but the results are very sensitive to motion.

The reults are displayed on a chart similar to an ECG chart, except the vertical values are not in mV but are arbitrarily scaled raw data. The data are plotted as the sum of the three PPG channels, each with the ambient channel subtracted. Only the last 30 seconds of data is retained.

There is a correction to remove the DC component with a moving average of the last several points. After recording, the plot is scaled to have 80% of the values fall within +/- two large blocks.

The data can be saved as an image or as a CSV file. 

It uses the Polar SDK located at  <https://github.com/polarofficial/polar-ble-sdk>.

**More information**

More information and FAQ are at <https://kennethevans.github.io> as well as more projects from the same author.

Licensed under the MIT license. (See: <https://en.wikipedia.org/wiki/MIT_License>)