#!/bin/bash

loaditems() {
    echo "Sending requests for Group 1..."
    
    curl -X POST http://localhost:8080/item-categories \
      -H "Content-Type: application/json" \
      -H "Accept: application/json" \
      -d '{
        "name": "test category3"
      }'
    echo
}

group2() {
    echo "Sending requests for Group 2..."
}

group3() {
    echo "Sending requests for Group 3..."
}

if [ "$1" == "group1" ]; then
    group1
elif [ "$1" == "group2" ]; then
    group2
elif [ "$1" == "group3" ]; then
    group3
else
    echo "Invalid group. Please specify one of the following: group1, group2, or group3."
    exit 1
fi

