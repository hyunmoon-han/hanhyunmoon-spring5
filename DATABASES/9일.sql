SELECT
    ROWNUM AS rnum,
    tablea.*
FROM ( SELECT
    *
FROM
    tbl_member <if 
test= "search_type !='' and search_type !=null"> WHERE <if test="search_type == 'all'.toString()"> 
user_id LIKE '%'||'admin'||'%' OR user_name LIKE '%'||'admin'||'%' </if> <if test="search_type 
== 'user_id'.toString()"> user_id LIKE '%'||'admin'||'%' </if> <if test="search_type == 'user_name'.toString()"> 
user_name LIKE '%'||'admin'||'%' </if> </if>
ORDER BY
    reg_date DESC
) TableA
WHERE
    rownum <= ( 0 * 10 ) + 10
) TableB
WHERE
    tableb.rnum > 0 * 10 