$( document ).ready( () => {
	loadCharts();
});

function loadCharts() {
	console.log("Loading charts");
	$.get("/api/messages/info", (data) => {
		console.log(data);
		$( "#totalMessageCount" ).append(data.messageCount);
		$( "#firstMessage" ).append(data.firstMessage);
		$( "#lastMessage" ).append(data.lastMessage);
	});
}
