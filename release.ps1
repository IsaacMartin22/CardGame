$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot

try {
    $status = git status --porcelain --untracked-files=normal
    if ($LASTEXITCODE -ne 0) { throw 'Could not read Git status.' }
    if ($status) { throw 'Commit or stash all changes before creating a release.' }

    $branch = git symbolic-ref --quiet --short HEAD
    if ($LASTEXITCODE -ne 0) { throw 'A checked-out branch is required to create a release.' }
    $remote = git config --get "branch.$branch.remote"
    if ($LASTEXITCODE -ne 0 -or -not $remote) { throw "Branch '$branch' has no upstream remote." }
    $upstream = git config --get "branch.$branch.merge"
    if ($LASTEXITCODE -ne 0 -or -not $upstream) { throw "Branch '$branch' has no upstream branch." }

    $versionPath = 'core\src\main\resources\game-version.properties'
    $content = [IO.File]::ReadAllText((Join-Path $PSScriptRoot $versionPath))
    $match = [regex]::Match($content, '\Aversion=(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\r?\ndate=\d{4}-\d{2}-\d{2}\r?\n?\z')
    if (-not $match.Success) { throw "Invalid version metadata in $versionPath." }

    $major = [int]$match.Groups[1].Value
    $minor = [int]$match.Groups[2].Value
    $patch = [int]$match.Groups[3].Value
    $choice = (Read-Host "Current version $major.$minor.$patch. Release major, minor, or patch").Trim().ToLowerInvariant()
    switch ($choice) {
        'major' { $major++; $minor = 0; $patch = 0 }
        'minor' { $minor++; $patch = 0 }
        'patch' { $patch++ }
        default { throw 'Choose major, minor, or patch; no release was created.' }
    }

    $version = "$major.$minor.$patch"
    $date = Get-Date -Format 'yyyy-MM-dd'
    [IO.File]::WriteAllText((Join-Path $PSScriptRoot $versionPath), "version=$version`ndate=$date`n", [Text.Encoding]::ASCII)
    git add -- $versionPath
    if ($LASTEXITCODE -ne 0) { throw "Could not stage $versionPath; review the working tree." }
    git commit -m "Release $version"
    if ($LASTEXITCODE -ne 0) { throw 'Release commit failed; review the staged version change before retrying.' }
    git push $remote "HEAD:$upstream"
    if ($LASTEXITCODE -ne 0) { throw "Release $version was committed but not pushed; resolve the error and run git push $remote HEAD:$upstream." }
    Write-Host "Released $version ($date)."
} finally {
    Pop-Location
}
