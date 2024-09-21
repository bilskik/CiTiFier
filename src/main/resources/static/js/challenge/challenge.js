function applyErrorClass(githubLinkInput) {
    htmx.addClass(githubLinkInput, 'is-invalid');
    htmx.removeClass(githubLinkInput, 'is-valid');
    const p = htmx.find('#htmx-github-link-error');
    if(p) {
        htmx.addClass(p, "invalid-feedback");
        htmx.removeClass(p, "valid-feedback");
    }
}

function applyValidClass(githubLinkInput) {
    htmx.addClass(githubLinkInput, 'is-valid');
    htmx.removeClass(githubLinkInput, 'is-invalid');
    const p = htmx.find('#htmx-github-link-error');
    if(p) {
        htmx.addClass(p, "valid-feedback");
        htmx.removeClass(p, "invalid-feedback");
    }
}


document.addEventListener('DOMContentLoaded', () => {
    const githubLinkInput = htmx.find('#github-link');
    const privateGithubLinkCloneInfo = htmx.find('#private-github-clone-info');
    const p = htmx.find('#htmx-github-link-error');

    if(githubLinkInput && privateGithubLinkCloneInfo && p) {
        const state = privateGithubLinkCloneInfo.dataset.state;
        const info = privateGithubLinkCloneInfo.dataset.info;
        if(state === "OK") {
            applyValidClass(githubLinkInput);
            p.innerHTML = info;
        } else if(state === "ERROR") {
            applyErrorClass(githubLinkInput);
            p.innerHTML = info;
        }
    }
})

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

function handleAfterRequest(event) {
    const githubLinkInput = htmx.find('#github-link');
    if(!githubLinkInput) {
        return;
    }

    if(event.detail.failed) {
        applyErrorClass(githubLinkInput);
    } else {
        applyValidClass(githubLinkInput);
    }
    const p = htmx.find('#htmx-github-link-error');
    if(p) {
        p.innerHTML = event.detail.xhr.response;
    }
}


