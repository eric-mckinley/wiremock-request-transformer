#!/bin/bash
echo '{ "request": { "urlPattern": ".*", "method": "ANY" }, "response": { "status": 404, "body": "' > /tmp/trimmed.tmp &&
tr -d '\r\n' < /tmp/404.vm >> /tmp/trimmed.tmp &&
echo '", "transformers": ["body-transformer"] }}' >> /tmp/trimmed.tmp &&
mkdir /opt/mappings &&
tr -d '\r\n' < /tmp/trimmed.tmp >> /opt/mappings/trimmed.json
