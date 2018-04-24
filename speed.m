function speed

  v = load('-ascii', 'speeds.data');
  plot(v(:,1), v(:,2), 'color', 'r');
  xlabel('Tiempo (s)');
  ylabel('Velocidad (m/s)');
end
