#!/bin/bash
docker run --rm  -it -v "${PWD}:/code" -p 80:9000 madsequipo14/mads-todolist-2017:0.2
