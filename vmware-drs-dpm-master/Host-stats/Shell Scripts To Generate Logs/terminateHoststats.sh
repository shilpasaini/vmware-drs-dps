#!/bin/bash

echo ""
arr=( $(head runHOSTStatsPID.txt))
kill $arr
