#!/bin/bash

echo ""
arr=( $(head runVMStatsPID.txt))
kill $arr
