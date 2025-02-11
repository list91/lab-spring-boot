# Script for testing REST API products

# Settings
$baseUrl = "http://localhost:8081/api/products"

# Function for executing Invoke-WebRequest with error handling
function Test-ApiRequest {
    param(
        [string]$Url,
        [string]$Method = 'Get',
        [object]$Body = $null
    )

    try {
        $headers = @{
            'Content-Type' = 'application/json'
        }

        if ($Body) {
            $jsonBody = $Body | ConvertTo-Json
            $response = Invoke-WebRequest -Uri $Url -Method $Method -Body $jsonBody -Headers $headers
        }
        else {
            $response = Invoke-WebRequest -Uri $Url -Method $Method -Headers $headers
        }

        Write-Host "Request $Method to $Url successful:" -ForegroundColor Green
        $response.Content | ConvertFrom-Json | Format-List
        return $response
    }
    catch {
        Write-Host "Error executing request to $Url:" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        return $null
    }
}

# Initialization of test data
Write-Host "1. Initialization of test products" -ForegroundColor Cyan
Test-ApiRequest -Url "$baseUrl/init" -Method Post

# Getting all products
Write-Host "`n2. Getting list of all products" -ForegroundColor Cyan
Test-ApiRequest -Url $baseUrl

# Getting product by ID
Write-Host "`n3. Getting product by ID" -ForegroundColor Cyan
Test-ApiRequest -Url "$baseUrl/1"

# Creating new product
Write-Host "`n4. Creating new product" -ForegroundColor Cyan
$newProduct = @{
    name = "Card of Unicorn"
    description = "Mythical card with unicorn image"
    price = 299.99
    category = "Mythical Creatures"
    stockQuantity = 3
}
Test-ApiRequest -Url $baseUrl -Method Post -Body $newProduct

# Getting updated list of products
Write-Host "`n5. Getting updated list of products" -ForegroundColor Cyan
Test-ApiRequest -Url $baseUrl
