#!/bin/bash

# Specify the fixed directory path
directory="../data/"

# Check if the provided path is a directory
if [ ! -d "$directory" ]; then
    echo "Error: '$directory' cannot be found"
    exit 1
fi

# Delete all files in the directory
find "$directory" -mindepth 1 -type d -exec rm -r {} \;
echo "All conentent in data has been deleted"
rm -rf $directory
echo "main dir "$directory" has been deleted";
