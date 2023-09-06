#!/bin/bash

# Specify the fixed directory path
directory="../data"

# Check if the provided path is a directory
if [ ! -d "$directory" ]; then
    echo "Error: '$directory' is not a directory."
    exit 1
fi

# Delete all files in the directory
find "$directory" -mindepth 1 -type d -exec rm -r {} \;
echo "All files in '$directory' have been deleted."