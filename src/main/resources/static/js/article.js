window.onload=function () {
    compile();
};

function compile() {
    var text = document.getElementById("article-markdown").innerHTML;
    var converter = new showdown.Converter();
    var article = converter.makeHtml(text);
    document.getElementsByClassName("article")[0].innerHTML = article;
}