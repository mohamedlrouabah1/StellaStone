$(document).ready(function () {
    $("#searchInput").on("input", function () {
        var query = $(this).val();
        if (query.length >= 3) {
            $.get("/search?q=" + query, function (data) {
                var results = "";
                for (var i = 0; i < data.length; i++) {
                    results += "<div><a href='" + data[i].url + "'>" + data[i].name + "</a></div>";
                }
                $("#searchResults").html(results);
            });
        } else {
            $("#searchResults").html("");
        }
    });
});
