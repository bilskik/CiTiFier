function handleAfterSwap(e) {
    const privateGithubLinkSubmit = htmx.find('#challenge-github-link-button');
    const githubLinkInput = htmx.find('#github-link')
    if(privateGithubLinkSubmit && githubLinkInput) {
        htmx.on(githubLinkInput, "change", (event) => {
            const value = event.target.value;
            const href = privateGithubLinkSubmit.href;
            const parts = href ? href.split('state=') : undefined;
            if(parts && parts.length === 2) {
                privateGithubLinkSubmit.href = parts[0] + "state=" + value;
            }
        })
    }
}
