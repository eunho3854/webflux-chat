<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<style>
    .container {
        width: 80%;
        margin: 0 auto;
        border: 1px solid black;
    }
</style>
<body>
    <div>알림 : <span id="notify">1</span></div>
<div class="container">
    <div id="items"></div>
</div>
    
<div>
    <input id = "inputText"/>
    <button id = "inputBtn">전송</button>
</div>


<script>
    fetch("http://localhost:8080/").then(res => res.json()).then(res=>{
        console.log(res);

        let items_el = document.querySelector("#items");
        
        for(i of res) {
        let item_el = document.createElement("div");
        item_el.innerHTML = `게시글 ${i}`;
        items_el.appendChild(item_el)
        }
    });

    const eventSource = new EventSource("http://localhost:8080/sse");
    eventSource.onmessage = event => {
        console.log(event.data);
        let notify_el = document.querySelector("#notify");
        console.log(notify_el.textContent);
        let num = Number(notify_el.textContent);
        num = num +1;
        notify_el.innerHTML = num;

        let text_el = document.querySelector("#inputText");
        console.log(text_el.textContent);
    };
    eventSource.onerror = error => {
        eventSource.close();
    }
</script>    
</body>
</html>