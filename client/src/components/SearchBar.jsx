import React, { useState } from 'react';
import { Box, TextField, Button, InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { checkPincodeValidity } from '../services/restaurantService';

const SearchBar = ({ onSearch }) => {
  const [query, setQuery] = useState('');

  const validateSearchQuery = (query) => {
    query = query.trim();
    const validCharacters = /^[a-zA-Z0-9\s]*$/;
    const isNumeric = /^\d+$/.test(query);

    if (query.length > 30) return false;

    if (isNumeric && query.length < 10) {
      return checkPincodeValidity(query)
        .then((result) => result)
        .catch((error) => {
          console.error('Error during checkPincodeValidity:', error);
          return false;
        });
    }

    if (!validCharacters.test(query)) return false;

    return true;
  };

  const handleSearch = async () => {
    const isValid = await validateSearchQuery(query);
  
    if (isValid) {
      onSearch(query, true); // Pass the actual query as the first argument
    } else {
      onSearch(query, false);
    }
  };
  
  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        width: '100%',
        maxWidth: '900px',
        margin: '20px auto',
        borderRadius: '30px',
        backgroundColor: '#fff',
        boxShadow: '0px 2px 6px rgba(0, 0, 0, 0.15)',
      }}
    >
      <TextField
        fullWidth
        variant="outlined"
        placeholder="Restaurants Name or Zip Code"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon sx={{ color: 'rgba(0, 0, 0, 0.54)' }} />
            </InputAdornment>
          ),
          sx: {
            '& .MuiOutlinedInput-root': {
              '& fieldset': { border: 'none' },
            },
            borderRadius: '30px 0 0 30px',
            paddingLeft: '15px',
            paddingRight: '15px',
          },
        }}
        sx={{ flexGrow: 1 }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={handleSearch}
        sx={{
          height: '56px',
          minWidth: '60px',
          borderRadius: '0 30px 30px 0',
          backgroundColor: '#d32323',
          '&:hover': { backgroundColor: '#b81e1e' },
        }}
      >
        <SearchIcon />
      </Button>
    </Box>
  );
};

export default SearchBar;
