# Service Design Description
**fetch-detections**

Returns all the positive detection what were found from a specified start time.

**Endpoint:** HTTP 1.1 GET /ml-provider/detections

**Input:** HTTP query parameter

`?from={timestamp}`

Where timestamp is an epoch timestamp. (Number representation of the seconds passed from 1970.01.01 00:00 UTC)

**Output:** HTTP response 

Status Code:
```
200 (OK), 400 (Bad request), 401 (Not authorized), 500 (Internal server error)
```

Headers:
```
Content-Type: application/json
```


Payload:
```
[
  {
    "timestamp": number,
    "location": string,
  },
  ...
]
```

**Example**

Request:
http://127.0.01:10000/ml-provider/detections?from=1694606117

Response:

HTTP 200 (OK)

```
[
  {
    "timestamp": 1694606125,
    "location": "A1",
  },
  {
    "timestamp": 1694606118,
    "location": "A3",
  }
]
```
