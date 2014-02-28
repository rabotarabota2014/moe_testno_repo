#!/bin/bash




#sort -u -k6,6 2014_02_28_pureftpd.log.1 | awk -F" " '{print $6 }'





#IFS=$'\n' 


#sort -u -k6,6 "${1}" | awk -F" " '{print $6 }'

#IFS=$'\n' #y=($x)
#niza=$(sort -u -k6,6 "${1}" | awk -F" " '{print $6 }')
sort -u -k6,6 "${1}" | awk -F" " '{print $6 }' > temp.tmp

while read line
do
    name=$line
    echo "ZA:	""$name"
if [[ $name == "(?"* ]]
then
	echo "	odmaraj"
else
	grep "$name" "${1}"  >> "file_"$name
fi
	
done < temp.tmp

rm temp.tmp

#echo $niza

#for var in "${niza[@]}"
#do
#  echo "_"${var}"_"
#done

#echo "${niza[@]}"


#for file in "${niza[@]}"; do
#     echo "aaa"   "$file" "aaa"
#done
