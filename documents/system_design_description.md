**Way of working**

Quartz job:

- Reads the input folders (input_path_prefix + location) in every iteration.
- Moves the files found in the input folder into the appropriate woking folder (working_path_prefix + location) and renames the files using the current time as file name.
- Adds the new file path to a processing queue.
	
Worker thread:

- Takes the first item from the processing queue.
- Executes the ML algorithm on the given item (image file path)
- Parses the output of the ML algorithm and saves into the database, but only when the algorithm reports human on the image and the confidence is greater than or equals to the given threshold.

API:

- Implements the `fetch-detections` service using the database

**Database**

In memory H2 database:

Detection table:

- id (bigint)
- timestamp (bigint)
- location (varchar)

**Application properties**

locations: [A1, A2...]<br />
confidence_threshold: 80<br />
read_interval: 1000<br />
input_path_prefix: /tmp/aims-demo/input<br />
working_path_prefix: /tmp/aims-demo/working<br />
